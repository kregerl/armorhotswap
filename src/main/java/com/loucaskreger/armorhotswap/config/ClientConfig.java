package com.loucaskreger.armorhotswap.config;

import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ClientConfig {
	public static BooleanValue preventCurses;

	public static ForgeConfigSpec.ConfigValue<List<? extends String>> itemBlacklist;

	public ClientConfig(ForgeConfigSpec.Builder builder) {
		builder.push("Enchantments");
		preventCurses = builder.comment("Prevents Armor Hotswap from equipping armor with a curse on it.")
				.translation("armorhotswap.config.preventcurses").define("preventCurses", true);
		builder.pop();
		builder.push("Item Blacklist");
		itemBlacklist = builder.comment(
				"List of items that will be blacklisted when swapping armor. Ex: [\"minecraft:diamond_chestplate\", \"minecraft:pumpkin\"]")
				.translation("armorhotswap.config.blacklist")
				.defineList("itemBlacklist", Arrays.asList(""), i -> i instanceof String);
		builder.pop();
	}

}
