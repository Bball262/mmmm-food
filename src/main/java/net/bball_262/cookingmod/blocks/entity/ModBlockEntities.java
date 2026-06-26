package net.bball_262.cookingmod.blocks.entity;

import net.bball_262.cookingmod.CookingMod;
import net.bball_262.cookingmod.blocks.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, CookingMod.MOD_ID);
    public static final Supplier<BlockEntityType<CookingPanBlockEntity>> COOKING_PAN_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("cooking_pan_block_entity", () -> BlockEntityType.Builder.of(
                    CookingPanBlockEntity::new, ModBlocks.COOKING_PAN.get()).build(null));
}