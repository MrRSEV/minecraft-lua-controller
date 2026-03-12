// This is an adapter, 
// to exchange data from different forge repos to the common repos

package de.rsev.minecraft.forge.controller.lua.bridge.player;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import de.rsev.minecraft.forge.controller.lua.LuaRuntime;

public class ForgeLuaPlayerAdapter implements LuaPlayerAdapter {

    // =========================
    // getById
    // =========================
    @Override
    public LuaPlayerWrapper getPlayer(int playerId) {

        var player = LuaPlayerLookup.find(playerId);

        if (player == null) {
            return null;
        }

        return new LuaPlayerWrapper(new ForgeLuaPlayerHandle(player));
    }

    // =========================
    // getAllPlayers
    // =========================
    @Override
    public LuaValue getAllPlayers() {

        LuaTable table = new LuaTable();

        int i = 1;

        var server = LuaRuntime.getServer();
        if (server == null) return table;

        var mc = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
        
        if (mc == null) return table;

        for (var p : mc.getPlayerList().getPlayers()) {

            LuaPlayerWrapper wrapper =
                    new LuaPlayerWrapper(new ForgeLuaPlayerHandle(p));

            table.set(i++, LuaPlayerBridge.wrap(wrapper));
        }

        return table;
    }

    // =========================
    // addItem
    // =========================
    @Override
    public boolean addItem(int playerId, String itemId, int count) {
        return LuaPlayerBridgeInternal.addItem(playerId, itemId, count);
    }

    // =========================
    // removeItem
    // =========================
    @Override
    public boolean removeItem(int playerId, String itemId, int count) {
        return LuaPlayerBridgeInternal.removeItem(playerId, itemId, count);
    }

    // =========================
    // sendMessage
    // =========================
    @Override
    public boolean sendMessage(int playerId, String message) {
        return LuaPlayerBridgeInternal.sendMessage(playerId, message);
    }
}