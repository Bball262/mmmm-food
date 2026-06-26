package net.bball_262.cookingmod.recipes.custom;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public class CookingRecipeInput implements RecipeInput {
    private final List<ItemStack> items;

    private CookingRecipeInput(List<ItemStack> items) {
        this.items = items;
    }

    @Override
    public ItemStack getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public int size() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        return RecipeInput.super.isEmpty();
    }
}
