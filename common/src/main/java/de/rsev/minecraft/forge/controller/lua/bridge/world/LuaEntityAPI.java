package de.rsev.minecraft.forge.controller.lua.bridge.world;

public class LuaEntityAPI {

    private static LuaEntityAdapter ADAPTER;

    public static void setAdapter(LuaEntityAdapter adapter) {
        ADAPTER = adapter;
    }

    public boolean spawn(String entityId, double x, double y, double z) {
        return ADAPTER.spawn(entityId, x, y, z);
    }
}