package com.loucaskreger.com;

import net.minecraftforge.fml.common.Mod;
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

	}

	private void setupCommon(final FMLCommonSetupEvent event) {

	}

	private void setupClient(final FMLClientSetupEvent event) {

	}

}
