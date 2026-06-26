package net.bball_262.cookingmod;

import net.bball_262.cookingmod.blocks.ModBlocks;
import net.bball_262.cookingmod.blocks.entity.ModBlockEntities;
import net.bball_262.cookingmod.components.ModComponents;
import net.bball_262.cookingmod.items.ModItems;
import net.bball_262.cookingmod.screen.ModMenuTypes;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(CookingMod.MOD_ID)
public class CookingMod {
    public static final String MOD_ID = "cookingmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CookingMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModComponents.COMPONENTS.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}