package de.rsev.minecraft.forge.controller.lua.bridge.world;

public class LuaBlockAPI {

    private static LuaWorldAdapter ADAPTER;

    public static void setAdapter(LuaWorldAdapter adapter) {
        ADAPTER = adapter;
    }

    public boolean setBlock(int x, int y, int z, String blockId) {
        return ADAPTER.setBlock(x, y, z, blockId);
    }
}