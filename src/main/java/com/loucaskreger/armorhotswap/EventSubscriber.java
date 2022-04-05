package com.loucaskreger.armorhotswap;

import com.loucaskreger.armorhotswap.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = ArmorHotswap.MOD_ID, value = Dist.CLIENT)
public class EventSubscriber {

    private static final int MAX_ARMOR_INDEX = 5;

    @SubscribeEvent
    public static void onPlayerRightClick(final PlayerInteractEvent.RightClickItem event) {
        if (event.getWorld().isClientSide()) {

            Minecraft mc = Minecraft.getInstance();
            var player = mc.player;
            var pc = mc.gameMode;

            if (event.getHand() == InteractionHand.MAIN_HAND && !player.isCrouching()) {
                System.out.println("Clicked");

                ItemStack stack = player.getMainHandItem();

                if (isBlacklisted(stack)) {
                    return;
                }

                if (ClientConfig.preventCurses.get() && hasCurse(stack)) {
                    return;
                }

                if (mc.hitResult.getType() == HitResult.Type.ENTITY) {
                    return;
                }

                if (mc.hitResult.getType() == HitResult.Type.BLOCK) {
                    if (stack.getItem() instanceof PlayerHeadItem || stack.getItem() instanceof StandingAndWallBlockItem) {
                        return;
                    } else if (stack.getItem() instanceof BlockItem) {
                        BlockItem blockItem = (BlockItem) stack.getItem();
                        if (blockItem.getBlock() instanceof CarvedPumpkinBlock) {
                            return;
                        }
                    }
                }

                ItemStack offhandStack = player.getOffhandItem();
                if (isBlacklisted(offhandStack)) {
                    return;
                }
                int currentItemIndex = player.getInventory().items.indexOf(stack);
                var equipmentSlotType = Monster.getEquipmentSlotForItem(stack);
                int armorIndexSlot = determineIndex(equipmentSlotType);
                SoundEvent sound = getSoundEvent(stack);

                if (armorIndexSlot < 0 && !offhandStack.isEmpty()) {
                    currentItemIndex = 40;
                    equipmentSlotType = Monster.getEquipmentSlotForItem(offhandStack);
                    armorIndexSlot = determineIndex(equipmentSlotType);
                    sound = getSoundEvent(offhandStack);
                }

                if (armorIndexSlot > 0) {
                    if (sound != null) {
                        player.playSound(sound, 1.0f, 1.0f);
                    } else {
                        player.playSound(stack.getItem() == Items.ELYTRA ? SoundEvents.ARMOR_EQUIP_ELYTRA
                                : SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                    }
                    if (player.getInventory().getArmor(3 - (armorIndexSlot - MAX_ARMOR_INDEX)).isEmpty()) {
                        return;
                    }
                    pc.handleInventoryMouseClick(player.inventoryMenu.containerId, armorIndexSlot, currentItemIndex, ClickType.SWAP,
                            player);
                }
            }
        }
    }

    private static SoundEvent getSoundEvent(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ArmorItem) {
            return ((ArmorItem) item).getMaterial().getEquipSound();
        }

        return null;
    }

    private static boolean hasCurse(ItemStack stack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        if (enchantments.containsKey(Enchantments.BINDING_CURSE)
                || enchantments.containsKey(Enchantments.VANISHING_CURSE)) {
            return stack.getItem() instanceof ArmorItem;
        }
        return false;
    }

    /**
     * @param type
     * @return the index of the desired armor slot, otherwise -1
     */

    private static int determineIndex(EquipmentSlot type) {
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

    private static boolean isBlacklisted(ItemStack stack) {
        for (String registryName : ClientConfig.itemBlacklist.get()) {
            if (stack.getItem().getRegistryName().toString().equals(registryName)) {
                return true;
            }
        }
        return false;
    }

}
