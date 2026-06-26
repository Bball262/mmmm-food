package net.bball_262.cookingmod.blocks.entity;

import net.bball_262.cookingmod.components.ModComponents;
import net.bball_262.cookingmod.items.ModItems;
import net.bball_262.cookingmod.screen.custom.CookingPanMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class CookingPanBlockEntity extends BlockEntity implements MenuProvider {
    public final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    private final int[] INPUT_SLOTS = {0, 1, 2};
    private final int OUTPUT_SLOT = 3;
    protected final ContainerData data;
    private int progress = 0;
    private int max_progress = 72;

    public CookingPanBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.COOKING_PAN_BLOCK_ENTITY.get(), pos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> CookingPanBlockEntity.this.progress;
                    case 1 -> CookingPanBlockEntity.this.max_progress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int value) {
                switch (i) {
                    case 0: CookingPanBlockEntity.this.progress = value;
                    case 1: CookingPanBlockEntity.this.max_progress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (hasRecipe()) {
            progress++;
            setChanged(level, blockPos, blockState);

            if (craftingFinished()) {
                craftItem();
                resetProgress();
            }

        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
        max_progress = 72;
    }

    private void craftItem() {
        ItemStack input = new ItemStack(Items.BEEF);

        if (!itemHandler.getStackInSlot(0).isEmpty() || itemHandler.getStackInSlot(0).equals(input)) {
            input = itemHandler.extractItem(INPUT_SLOTS[0], 1, false);
        } else if (!itemHandler.getStackInSlot(1).isEmpty() || itemHandler.getStackInSlot(1).equals(input)) {
            input = itemHandler.extractItem(INPUT_SLOTS[1], 1, false);
        } else if (!itemHandler.getStackInSlot(2).isEmpty() || itemHandler.getStackInSlot(2).equals(input)) {
            input = itemHandler.extractItem(INPUT_SLOTS[2], 1, false);
        }

        ItemStack output = new ItemStack(Items.BEEF);

        switch (input.get(ModComponents.COOKING_PERCENTAGE)) {
            case 2: output.set(ModComponents.COOKING_PERCENTAGE, 3);
            case 3: output.set(ModComponents.COOKING_PERCENTAGE, 4);
            case 4: output.set(ModComponents.COOKING_PERCENTAGE, 5);
            default: output.set(ModComponents.COOKING_PERCENTAGE, 2);
        }

        itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(output.getItem(),
                itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + output.getCount()));
    }

    private boolean craftingFinished() {
        return this.progress >= this.max_progress;
    }

    public boolean hasRecipe() {
        ItemStack output = new ItemStack(Items.COOKED_BEEF);

        if (canAddToOutput(output)) {
            if (!itemHandler.getStackInSlot(0).isEmpty() || itemHandler.getStackInSlot(0).equals(output)) {
                return true;
            } else if (!itemHandler.getStackInSlot(1).isEmpty() || itemHandler.getStackInSlot(1).equals(output)) {
                return true;
            } else if (!itemHandler.getStackInSlot(2).isEmpty() || itemHandler.getStackInSlot(2).equals(output)) {
                return true;
            }
        }

        return false;
    }

    private boolean canAddToOutput(ItemStack output) {
        int maxCount = itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ? 64 : itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
        int currentCount = itemHandler.getStackInSlot(OUTPUT_SLOT).getCount();

        return itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                itemHandler.getStackInSlot(OUTPUT_SLOT).getItem().equals(output.getItem()) &&
                maxCount >= currentCount + output.getCount();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tag.put("inventory", itemHandler.serializeNBT(registries));
        tag.putInt("cooking_pan.progress", progress);
        tag.putInt("cooking_pan.max_progress", max_progress);

        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        itemHandler.deserializeNBT(registries, tag.getCompound("inventory"));
        progress = tag.getInt("cooking_pan.progress");
        max_progress = tag.getInt("cooking_pan.max_progress");

        super.loadAdditional(tag, registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.cookingmod.cooking_pan");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new CookingPanMenu(i, inventory, this, this.data);
    }
}
