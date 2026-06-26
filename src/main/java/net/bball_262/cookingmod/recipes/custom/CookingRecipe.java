package net.bball_262.cookingmod.recipes.custom;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public record CookingRecipe(ItemStack result, NonNullList<ItemStack> ingredients, int cookingTime) implements Recipe<CookingRecipeInput> {
    public boolean matches(CookingRecipeInput cookingRecipeInput, Level level) {
        return false;
    }

    public ItemStack assemble(CookingRecipeInput cookingRecipeInput, HolderLookup.Provider provider) {
        return null;
    }

    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return null;
    }

    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    public RecipeType<?> getType() {
        return null;
    }

    public static class Serializer implements RecipeSerializer<CookingRecipe> {
        private static final MapCodec<CookingRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").forGetter((recipe) -> {
                    List<Ingredient> ingredients = List.of();
                    for (int i = 0; i < recipe.ingredients.size(); i++) {
                        ingredients.add(Ingredient.of(recipe.ingredients.get(i)));
                    }
                    return ingredients;
                }),
                Ingredient.CODEC.fieldOf("result").forGetter(Ingredient.of(CookingRecipe::result))
        ).apply(inst, CookingRecipe::new));

        @Override
        public MapCodec<CookingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CookingRecipe> streamCodec() {
            return null;
        }
    }
}