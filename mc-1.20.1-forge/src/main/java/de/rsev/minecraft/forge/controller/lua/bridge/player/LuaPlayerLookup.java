package de.rsev.minecraft.forge.controller.lua.bridge.player;

import net.minecraft.server.level.ServerPlayer;

public class LuaPlayerLookup {

    public static ServerPlayer find(int playerId) {

        var server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
        if (server == null) return null;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (player.getId() == playerId) {
                return player;
            }
        }

        return null;
    }
}
