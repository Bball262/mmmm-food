package net.bball_262.cookingmod.events;

import net.bball_262.cookingmod.components.ModComponents;
import net.bball_262.cookingmod.screen.ModMenuTypes;
import net.bball_262.cookingmod.screen.custom.CookingPanScreen;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

@EventBusSubscriber
public class ModEvents {
    @SubscribeEvent
    public static void modifyComponents(ModifyDefaultComponentsEvent event) {
        event.modify(Items.BEEF, (builder -> builder.set(ModComponents.COOKING_PERCENTAGE.get(), 1)));
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.COOKING_PAN_MENU.get(), CookingPanScreen::new);
    }
}
