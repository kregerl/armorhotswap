package com.loucaskreger.com;

import com.loucaskreger.com.config.ClientConfig;
import com.loucaskreger.com.config.Config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber(modid = ArmorHotswap.MOD_ID)
public class EventSubscriber {

	@SubscribeEvent
	public static void onPlayerInteract(final PlayerInteractEvent.RightClickItem event) {
		if (event.getItemStack().getItem() instanceof ArmorItem || event.getItemStack().getItem() == Items.ELYTRA) {
			Minecraft mc = Minecraft.getInstance();
			PlayerEntity player = mc.player;
			PlayerController pc = mc.playerController;
			RayTraceResult result = mc.objectMouseOver;

			if (!ClientConfig.swapBlacklist.get()
					.contains(player.inventory.getCurrentItem().getItem().getRegistryName().toString())) {
				switch (result.getType()) {

				case MISS:
				case BLOCK:
					swapMainhandArmor(mc, player, pc, event);
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
	private static void swapMainhandArmor(Minecraft mc, PlayerEntity player, PlayerController pc,
			PlayerInteractEvent.RightClickItem event) {
		if (event.getHand() == Hand.MAIN_HAND && event.getWorld().isRemote) {
			ItemStack stack = player.inventory.getCurrentItem();
			int currentItemIndex = player.inventory.mainInventory.indexOf(stack);
			EquipmentSlotType equipmentSlotType = MobEntity.getSlotForItemStack(stack);
			int armorIndexSlot = determineIndex(equipmentSlotType);

			if (armorIndexSlot != -1) {
				SoundEvent sound = getSoundEvent(stack);
				if (sound != null) {
					player.playSound(sound, 1f, 1f);
				} else {
					player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1f, 1f);
				}
				pc.windowClick(mc.player.container.windowId, armorIndexSlot, currentItemIndex, ClickType.SWAP, player);
			}
		}
	}

	// For offhand
//	private static void swapOffhandArmor(Minecraft mc, PlayerEntity player, PlayerController pc, Hand hand) {
//		ItemStack stack = player.getHeldItemOffhand();
//		// Offhand index
//		int currentItemIndex = 45;
//		EquipmentSlotType equipmentSlotType = MobEntity.getSlotForItemStack(stack);
//		int armorIndexSlot = determineIndex(equipmentSlotType);
//
//		if (armorIndexSlot != -1) {
//			SoundEvent sound = getSoundEvent(stack);
//			if (sound != null) {
//				player.playSound(sound, 1f, 1f);
//			} else {
//				player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1f, 1f);
//			}
//			System.out.println(armorIndexSlot);
//			System.out.println(currentItemIndex);
//			pc.windowClick(mc.player.container.windowId, armorIndexSlot, currentItemIndex, ClickType.SWAP, player);
//		}
//	}

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

	private static SoundEvent getSoundEvent(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ArmorItem) {
			return ((ArmorItem) item).getArmorMaterial().getSoundEvent();
		}

		return null;
	}

}
