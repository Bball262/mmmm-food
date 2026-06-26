package net.bball_262.cookingmod.mixins;

import net.bball_262.cookingmod.components.ModComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = Item.class)
public abstract class ItemMixin {
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void tooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag, CallbackInfo ci) {
        Item item = stack.getItem();
        DataComponentMap map = item.components();

        if (map.has(ModComponents.COOKING_PERCENTAGE.get())) {
            Component component;
            switch (map.getOrDefault(ModComponents.COOKING_PERCENTAGE.get(), 1)) {
                case 2: component = Component.literal("Medium Rare").withStyle(ChatFormatting.GRAY);
                case 3: component = Component.literal("Medium").withStyle(ChatFormatting.GRAY);
                case 4: component = Component.literal("Medium Well").withStyle(ChatFormatting.GRAY);
                case 5: component = Component.literal("Well Done").withStyle(ChatFormatting.GRAY);
                default: component = Component.literal("Rare").withStyle(ChatFormatting.GRAY);
            }
            tooltipComponents.add(component);
        }
    }
}