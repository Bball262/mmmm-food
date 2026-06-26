package net.bball_262.cookingmod.components;

import net.bball_262.cookingmod.CookingMod;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ModComponents {
    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.create(
            BuiltInRegistries.DATA_COMPONENT_TYPE, CookingMod.MOD_ID);
    public static final Supplier<DataComponentType<Integer>> COOKING_PERCENTAGE = register("cook_percentage",
            (builder) -> builder.persistent(ExtraCodecs.intRange(1, 5)).networkSynchronized(ByteBufCodecs.VAR_INT));

    public static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
        return COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());
    }
}
