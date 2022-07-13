package com.loucaskreger.armorhotswap.mixin;

import com.loucaskreger.armorhotswap.callbacks.KeyCallbacks;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardInputMixin {

    @Inject(method = "onKey", cancellable = true, at = @At(value = "FIELD", target = "Lnet/minecraft/client/Keyboard;debugCrashStartTime:J", ordinal = 0))
    private void onKeyboardInput(long windowPointer, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if (action == 1) {
            KeyCallbacks.KEY_PRESSED_EVENT.invoker().keyPressed(key, modifiers);
        } else if (action == 0) {
            KeyCallbacks.KEY_RELEASED_EVENT.invoker().keyReleased(key, modifiers);
        } else if (action == 2) {
            KeyCallbacks.KEY_HELD_EVENT.invoker().keyHeld(key, modifiers);
        }
    }
}
