package de.rsev.minecraft.forge.controller.lua.bridge.player;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import de.rsev.minecraft.forge.controller.lua.bridge.inventory.LuaInventoryBridgeInternal;

import net.minecraft.server.MinecraftServer;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class LuaPlayerBridgeInternal {

    private static MinecraftServer server() {
        return net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
    }

    // =========================
    // Players
    // =========================
    public static LuaTable getAllPlayers() {

        LuaTable table = new LuaTable();

        MinecraftServer server = server();
        if (server == null) return table;

        int i = 1;

        for (var player : server.getPlayerList().getPlayers()) {
            table.set(i++, CoerceJavaToLua.coerce(new LuaPlayerWrapper(new ForgeLuaPlayerHandle(player))));
        }

        return table;
    }

    public static LuaPlayerWrapper getById(int playerId) {

        MinecraftServer server = server();
        if (server == null) return null;

        for (var player : server.getPlayerList().getPlayers()) {

            if (player.getId() == playerId) {

                LuaLogger.info("LivePlayer gefunden → " + player.getName().getString());
                return new LuaPlayerWrapper(new ForgeLuaPlayerHandle(player));
            }
        }

        LuaLogger.info("❌ PlayerId nicht gefunden → " + playerId);
        return null;
    }


    // =========================
    // Inventory
    // =========================
    public static boolean addItem(int playerId, String itemId, int count) {

        LuaLogger.info("Lua → addItem player={} item={} count={}", playerId, itemId, count);

        try {
            return LuaInventoryBridgeInternal.addItem(playerId, itemId, count);
        } catch (Exception e) {
            LuaLogger.error("addItem failed", e);
            return false;
        }
    }

    public static boolean removeItem(int playerId, String itemId, int count) {

        LuaLogger.info("Lua → removeItem player={} item={} count={}", playerId, itemId, count);

        try {
            return LuaInventoryBridgeInternal.removeItem(playerId, itemId, count);
        } catch (Exception e) {
            LuaLogger.error("removeItem failed", e);
            return false;
        }
    }

    // =========================
    // Messaging
    // =========================
    public static boolean sendMessage(int playerId, String message) {

        try {
            LuaPlayerWrapper wrapper = getById(playerId);

            if (wrapper == null) return false;

            wrapper.sendMessage(message);
            return true;

        } catch (Exception e) {
            LuaLogger.error("sendMessage failed", e);
            return false;
        }
    }
}
