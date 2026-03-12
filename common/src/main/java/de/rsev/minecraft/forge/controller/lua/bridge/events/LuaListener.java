package de.rsev.minecraft.forge.controller.lua.bridge.events;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;

public abstract class LuaListener {

    private boolean once = false;

    public abstract void invoke(Object... args);

    public boolean isOnce() {
        return once;
    }

    public void setOnce(boolean once) {
        this.once = once;
    }

    public void handleError(String eventName, Exception e) {
        LuaLogger.forgeError("Lua listener error on event {}", eventName, e);
    }
}