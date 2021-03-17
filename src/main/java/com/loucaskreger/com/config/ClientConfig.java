package com.loucaskreger.com.config;

import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

	public static ForgeConfigSpec.ConfigValue<List<? extends String>> swapBlacklist;

	public ClientConfig(ForgeConfigSpec.Builder builder) {
		swapBlacklist = builder.comment("Items to blacklist when right clicking with armor hotswap.")
				.translation("pickpick.config.itemBlacklist")
				.defineList("blacklistItems", Arrays.asList(""), str -> str instanceof String);
		
	}

}
