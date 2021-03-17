package com.loucaskreger.com;

import com.loucaskreger.com.config.ClientConfig;
import com.loucaskreger.com.config.Config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = ArmorHotswap.MOD_ID, value = Dist.CLIENT)
public class EventSubscriber {

	@SubscribeEvent
	public static void onClientTick(final ClientTickEvent event) {
		ForgeIngameGui.renderFood = false;
	}

	@SubscribeEvent
	public static void onPlayerRightClick(final InputEvent.ClickInputEvent event) {
		if (event.isUseItem()) {
			Minecraft mc = Minecraft.getInstance();
			PlayerEntity player = mc.player;
			PlayerController pc = mc.playerController;
			RayTraceResult result = mc.objectMouseOver;
			if (!ClientConfig.swapBlacklist.get()
					.contains(player.inventory.getCurrentItem().getItem().getRegistryName().toString())) {
				switch (result.getType()) {

				case MISS:
				case BLOCK:
					swapArmor(mc, player, pc, event);
				case ENTITY:
					break;

				}
			}

		}
	}

	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
		if (configEvent.getConfig().getSpec() == Config.CLIENT_SPEC) {
			Config.bakeConfig();
		}
	}

	/**
	 * Swap the armor to the correct slots.
	 * 
	 * @param mc
	 * @param player
	 * @param pc
	 * @param event
	 */
	private static void swapArmor(Minecraft mc, PlayerEntity player, PlayerController pc,
			InputEvent.ClickInputEvent event) {
		ItemStack stack = player.inventory.getCurrentItem();
		int currentItemIndex = player.inventory.mainInventory.indexOf(stack);

		EquipmentSlotType equipmentSlotType = MobEntity.getSlotForItemStack(stack);
		int armorIndexSlot = determineIndex(equipmentSlotType);

		if (event.getHand() == Hand.MAIN_HAND && armorIndexSlot != -1) {
			player.playSound(stack.getItem() == Items.ELYTRA ? SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA
					: SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
			pc.windowClick(mc.player.container.windowId, armorIndexSlot, currentItemIndex, ClickType.SWAP, player);
		}
	}

	/**
	 * Inventory indicies go as follows: <br>
	 * Crafting Output: 0 <br>
	 * Crafting Input: 1-4 <br>
	 * Armor: 5-8 <br>
	 * Main Inventory: 9-35 <br>
	 * Hotbar: 36-44 <br>
	 * Offhand: 45 <br>
	 * 
	 * @param type
	 * @return the index of the desired armor slot, otherwise -1
	 */

	private static int determineIndex(EquipmentSlotType type) {
		switch (type) {
		case HEAD:
			return 5;
		case CHEST:
			return 6;
		case LEGS:
			return 7;
		case FEET:
			return 8;
		default:
			return -1;
		}
	}

}
