package com.loucaskreger.armorhotswap;

import com.loucaskreger.armorhotswap.config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.registry.Registry;

import java.util.Map;

public class ArmorHotswap implements ModInitializer {
	public static final String MOD_ID = "armorhotswap";

	@Override
	public void onInitialize() {
		Config.init();
		onRightClick();
	}


	private static void onRightClick() {
		UseItemCallback.EVENT.register((player, world, hand) -> {
			MinecraftClient mc = MinecraftClient.getInstance();
			ClientPlayerInteractionManager interactionManager = mc.interactionManager;


			if (!player.isSneaking()) {
				ItemStack stack;
				int currentItemIndex;
				if (hand == Hand.MAIN_HAND) {
					stack = player.getInventory().getMainHandStack();
					currentItemIndex = player.getInventory().main.indexOf(stack);
				} else {
					stack = player.getOffHandStack();
					currentItemIndex = 40;
				}

				if (isBlacklisted(stack)) {
					return TypedActionResult.fail(stack);
				}
				if (Config.INSTANCE.preventCurses && hasCurse(stack)) {
					return TypedActionResult.fail(stack);
				}

				if (mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
					return TypedActionResult.pass(stack);
				}

				EquipmentSlot equipmentSlotType = MobEntity.getPreferredEquipmentSlot(stack);
				var armorIndexSlot = determineIndex(equipmentSlotType);
				SoundEvent sound = getSoundEvent(stack);


				if (armorIndexSlot > 0) {
					if (sound != null) {
						player.playSound(sound, 1.0f, 1.0f);
					} else {
						player.playSound(stack.getItem() == Items.ELYTRA ? SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA
								: SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
					}
					interactionManager.clickSlot(player.playerScreenHandler.syncId, armorIndexSlot, currentItemIndex, SlotActionType.SWAP, player);
					return TypedActionResult.success(stack);
				}
			}
			return TypedActionResult.pass(ItemStack.EMPTY);
		});
	}

	private static SoundEvent getSoundEvent(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ArmorItem) {
			return ((ArmorItem) item).getMaterial().getEquipSound();
		}
		return null;
	}

	private static boolean hasCurse(ItemStack stack) {
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
		return enchantments.containsKey(Enchantments.BINDING_CURSE)
				|| enchantments.containsKey(Enchantments.VANISHING_CURSE);
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
	 * @param type The equipment slot
	 * @return the index of the desired armor slot, otherwise -1
	 */

	private static int determineIndex(EquipmentSlot type) {
		return switch (type) {
			case HEAD -> 5;
			case CHEST -> 6;
			case LEGS -> 7;
			case FEET -> 8;
			default -> -1;
		};
	}

	public static boolean isBlacklisted(ItemStack stack) {
		for (var name : Config.INSTANCE.itemBlacklist) {
			if (Registry.ITEM.getId(stack.getItem()).toString().equals(name)) {
				return true;
			}
		}
		return false;
	}

}