package de.rsev.minecraft.forge.controller.lua.bridge.events;

import java.util.*;

public class LuaEventBus {

    private static final Map<String, List<LuaListener>> LISTENERS = new HashMap<>();

    public static void register(String eventName, LuaListener listener) {
        LISTENERS.computeIfAbsent(eventName, k -> new ArrayList<>()).add(listener);
    }

    public static void emit(String eventName, Object... args) {
        List<LuaListener> listeners = LISTENERS.get(eventName);
        if (listeners == null) return;

        Iterator<LuaListener> it = listeners.iterator();

        while (it.hasNext()) {
            LuaListener listener = it.next();

            try {
                listener.invoke(args);
                if (listener.isOnce()) {
                    it.remove();
                }
            } catch (Exception e) {
                listener.handleError(eventName, e);
            }
        }
    }

    public static void clear() {
        LISTENERS.clear();
    }

    private LuaEventBus() {}
}
