package de.rsev.minecraft.forge.controller.lua.bridge.player;

public class LuaPlayerWrapper {

    private final LuaPlayerHandle handle;

    public LuaPlayerWrapper(LuaPlayerHandle handle) {
        this.handle = handle;
    }

    public String getName() {
        return handle.getName();
    }

    public int getId() {
        return handle.getId();
    }

    public double getX() {
        return handle.getX();
    }

    public double getY() {
        return handle.getY();
    }

    public double getZ() {
        return handle.getZ();
    }

    public void sendMessage(String message) {
        handle.sendMessage(message);
    }

    public Object getHandle() {
        return handle.getRaw();
    }
}