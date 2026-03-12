package de.rsev.minecraft.forge.controller.lua.bridge.events;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import org.luaj.vm2.*;

import java.util.*;

public class LuaEventsBridgeInternal {

    private static final Map<String, List<LuaValue>> EVENTS = new HashMap<>();

    public static void register(String event, LuaValue handler) {

        LuaLogger.info("Lua → Events.on '{}'", event);

        EVENTS.computeIfAbsent(event, k -> new ArrayList<>()).add(handler);
    }

    public static void fire(String event, LuaTable context) {

        List<LuaValue> handlers = EVENTS.get(event);
        if (handlers == null) return;

        for (LuaValue func : handlers) {
            try {
                func.call(context);
            } catch (Exception e) {
                LuaLogger.error("Event handler failed: " + event, e);
            }
        }
    }
}
