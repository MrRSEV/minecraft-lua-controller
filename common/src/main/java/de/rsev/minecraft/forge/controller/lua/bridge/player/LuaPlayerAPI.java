package de.rsev.minecraft.forge.controller.lua.bridge.player;

public class LuaPlayerAPI {

    private static LuaPlayerAdapter ADAPTER;

    public static void setAdapter(LuaPlayerAdapter adapter) {
        ADAPTER = adapter;
    }

    public LuaPlayerWrapper getById(int playerId) {
        return ADAPTER.getPlayer(playerId);
    }
}