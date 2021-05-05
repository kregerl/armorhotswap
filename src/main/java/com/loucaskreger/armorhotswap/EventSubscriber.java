package com.loucaskreger.armorhotswap;

import java.util.Map;

import com.loucaskreger.armorhotswap.config.ClientConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArmorHotswap.MOD_ID, value = Dist.CLIENT)
public class EventSubscriber {

	@SubscribeEvent
	public static void onPlayerRightClick(final InputEvent.ClickInputEvent event) {
		if (event.isUseItem()) {
			Minecraft mc = Minecraft.getInstance();
			PlayerEntity player = mc.player;
			PlayerController pc = mc.playerController;

			if (event.getHand() == Hand.MAIN_HAND) {

				ItemStack stack = player.getHeldItemMainhand();

				if (ClientConfig.preventCurses.get() && hasCurse(stack)) {
					return;
				}

				if (mc.objectMouseOver.getType() == RayTraceResult.Type.ENTITY) {
					return;
				}

				ItemStack offhandStack = player.getHeldItemOffhand();
				int currentItemIndex = player.inventory.mainInventory.indexOf(stack);
				EquipmentSlotType equipmentSlotType = MobEntity.getSlotForItemStack(stack);
				int armorIndexSlot = determineIndex(equipmentSlotType);
				SoundEvent sound = getSoundEvent(stack);

				if (armorIndexSlot < 0 && !offhandStack.isEmpty()) {
					currentItemIndex = 40;
					equipmentSlotType = MobEntity.getSlotForItemStack(offhandStack);
					armorIndexSlot = determineIndex(equipmentSlotType);
					sound = getSoundEvent(offhandStack);
				}

				if (armorIndexSlot > 0) {
					if (sound != null) {
						player.playSound(sound, 1.0f, 1.0f);
					} else {
						player.playSound(stack.getItem() == Items.ELYTRA ? SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA
								: SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
					}
					pc.windowClick(mc.player.container.windowId, armorIndexSlot, currentItemIndex, ClickType.SWAP,
							player);
				}
			}

		}
	}

	private static SoundEvent getSoundEvent(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof ArmorItem) {
			return ((ArmorItem) item).getArmorMaterial().getSoundEvent();
		}

		return null;
	}

	private static boolean hasCurse(ItemStack stack) {
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
		if (enchantments.containsKey(Enchantments.BINDING_CURSE)
				|| enchantments.containsKey(Enchantments.VANISHING_CURSE)) {
			return true;
		}
		return false;
	}

	/**
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