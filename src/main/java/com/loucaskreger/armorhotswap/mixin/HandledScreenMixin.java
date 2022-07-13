package com.loucaskreger.armorhotswap.mixin;

import com.loucaskreger.armorhotswap.ArmorHotswap;
import com.loucaskreger.armorhotswap.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(HandledScreen.class)
public class HandledScreenMixin extends Screen {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At(value = "TAIL"), method = "init()V", cancellable = true)
    private void onInit(CallbackInfo ci) {
        System.out.println("INIT");
    }

    @Inject(at = @At(value = "HEAD"), method = "mouseClicked", cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> ci) {
        IHandledScreenAccessor accessor = ((IHandledScreenAccessor) this);
        Slot slot = accessor.callGetSlotAt(mouseX, mouseY);
        ScreenHandler handler = accessor.getHandler();
        int slotIndex = handler.slots.indexOf(slot);
        System.out.println("HERE: " + slotIndex);
        if (slotIndex < 0) {
            ci.setReturnValue(false);
        } else {
            ItemStack stack = slot.getStack();
            EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(stack);
            int armorIndexSlot = ArmorHotswap.determineIndex(equipmentSlot);
            ClientPlayerInteractionManager interactionManager = mc.interactionManager;

            System.out.println(button);
            System.out.println(armorIndexSlot);
            System.out.println(ArmorHotswap.lCtrlPressed);
            if (button == 1 && armorIndexSlot != -1 && ArmorHotswap.lCtrlPressed) {
                System.out.println("HERE");
                if (Config.INSTANCE.preventCurses && ArmorHotswap.hasCurse(stack)) {
                    ci.setReturnValue(false);
                    return;
                }
                mc.player.playSound(stack.getItem() == Items.ELYTRA ? SoundEvents.ITEM_ARMOR_EQUIP_ELYTRA
                        : SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
                interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, armorIndexSlot, slotIndex,
                        SlotActionType.SWAP, mc.player);
                ci.cancel();
                ci.setReturnValue(true);
                System.out.println("END");
            }
        }
    }
}
