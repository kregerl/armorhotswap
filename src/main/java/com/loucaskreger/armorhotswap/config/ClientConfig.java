package com.loucaskreger.armorhotswap.config;

import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ClientConfig {
	public static BooleanValue preventCurses;

	public ClientConfig(ForgeConfigSpec.Builder builder) {
		preventCurses = builder.comment("Prevents Armor Hotswap from equipping armor with a curse on it.")
				.translation("armorhotswap.config.preventcurses").define("preventCurses", true);

	}

}
