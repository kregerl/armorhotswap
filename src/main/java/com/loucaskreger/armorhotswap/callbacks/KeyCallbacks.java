package com.loucaskreger.armorhotswap.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class KeyCallbacks {

    public static final Event<KeyPressed> KEY_PRESSED_EVENT = EventFactory.createArrayBacked(KeyPressed.class, listeners -> (key, modifiers) -> {
        for (KeyPressed listener : listeners) {
            listener.keyPressed(key, modifiers);
        }
    });

    public static final Event<KeyReleased> KEY_RELEASED_EVENT = EventFactory.createArrayBacked(KeyReleased.class, listeners -> (key, modifiers) -> {
        for (KeyReleased listener : listeners) {
            listener.keyReleased(key, modifiers);
        }
    });

    public static final Event<KeyHeld> KEY_HELD_EVENT = EventFactory.createArrayBacked(KeyHeld.class, listeners -> (key, modifiers) -> {
        for (KeyHeld listener : listeners) {
            listener.keyHeld(key, modifiers);
        }
    });


    @FunctionalInterface
    public interface KeyPressed {
        void keyPressed(int keyCode, int modifiers);
    }

    @FunctionalInterface
    public interface KeyReleased {
        void keyReleased(int keyCode, int modifiers);
    }

    @FunctionalInterface
    public interface KeyHeld {
        void keyHeld(int keyCode, int modifiers);
    }

}
