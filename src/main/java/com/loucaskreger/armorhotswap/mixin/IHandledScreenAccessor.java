package com.loucaskreger.armorhotswap.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HandledScreen.class)
public interface IHandledScreenAccessor {
    @Invoker
    Slot callGetSlotAt(double x, double y);

    @Accessor(value = "handler")
    ScreenHandler getHandler();
}
