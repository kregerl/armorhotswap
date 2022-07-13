package com.loucaskreger.armorhotswap;

import com.loucaskreger.armorhotswap.callbacks.KeyCallbacks;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import org.lwjgl.glfw.GLFW;

import java.util.Map;

public class ArmorHotswap implements ModInitializer {
    public static final String MOD_ID = "armorhotswap";
    public static boolean lCtrlPressed;

    @Override
    public void onInitialize() {
        Config.init();
        onRightClick();
        KeyCallbacks.KEY_PRESSED_EVENT.register((key, modifier) -> {
            if (key == GLFW.GLFW_KEY_LEFT_CONTROL) {
                lCtrlPressed = true;
            }
        });
        KeyCallbacks.KEY_RELEASED_EVENT.register((key, modifier) -> {
            if (key == GLFW.GLFW_KEY_LEFT_CONTROL) {
                lCtrlPressed = false;
            }
        });
    }

    private static void onRightClick() {
        UseItemCallback.EVENT.register((player, world, hand) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            ClientPlayerInteractionManager interactionManager = mc.interactionManager;

            if (mc.mouse.wasRightButtonClicked()) {

                ItemStack stack = player.getInventory().getMainHandStack();
                int currentItemIndex = player.getInventory().main.indexOf(stack);

                EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(stack);
                int armorIndexSlot = determineIndex(equipmentSlot);

                if (hand == Hand.MAIN_HAND && armorIndexSlot != -1) {
                    if (Config.INSTANCE.preventCurses && hasCurse(stack)) {
                        return TypedActionResult.fail(stack);
                    }
                    player.playSound(stack.getItem() == Items.ELYTRA ? SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA
                            : SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                    interactionManager.clickSlot(player.playerScreenHandler.syncId, armorIndexSlot, currentItemIndex,
                            SlotActionType.SWAP, player);
                    return TypedActionResult.success(stack);
                }
            }
            return TypedActionResult.pass(ItemStack.EMPTY);
        });
    }

    public static boolean hasCurse(ItemStack stack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
        if (enchantments.containsKey(Enchantments.BINDING_CURSE)
                || enchantments.containsKey(Enchantments.VANISHING_CURSE)) {
            return true;
        }
        return false;
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

    public static int determineIndex(EquipmentSlot type) {
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
