package com.loucaskreger.armorhotswap;

import com.loucaskreger.armorhotswap.config.Config;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ArmorHotswap.MOD_ID)
public class ArmorHotswap {
    public static final String MOD_ID = "armorhotswap";
    public static final Logger LOGGER = LogManager.getLogger();

    public ArmorHotswap() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupCommon);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
    }

    private void setupCommon(final FMLCommonSetupEvent event) {
    }

    private void setupClient(final FMLClientSetupEvent event) {
    }

}
