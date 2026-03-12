package de.rsev.minecraft.forge.controller.paper.lua.bridge.player;

import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerAdapter;
import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class PaperLuaPlayerAdapter implements LuaPlayerAdapter {

    @Override
    public LuaPlayerWrapper getPlayer(int playerId) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getEntityId() == playerId) {
                return new LuaPlayerWrapper(new PaperLuaPlayerHandle(player));
            }
        }
        return null;
    }

    @Override
    public LuaValue getAllPlayers() {
        LuaTable table = new LuaTable();
        int index = 1;

        for (Player player : Bukkit.getOnlinePlayers()) {
            table.set(index++, LuaPlayerBridge.wrap(new LuaPlayerWrapper(new PaperLuaPlayerHandle(player))));
        }

        return table;
    }

    @Override
    public boolean addItem(int playerId, String itemId, int count) {
        Player player = getBukkitPlayer(playerId);
        Material material = resolveMaterial(itemId);
        if (player == null || material == null) {
            return false;
        }

        player.getInventory().addItem(new ItemStack(material, count));
        return true;
    }

    @Override
    public boolean removeItem(int playerId, String itemId, int count) {
        Player player = getBukkitPlayer(playerId);
        Material material = resolveMaterial(itemId);
        if (player == null || material == null) {
            return false;
        }

        if (!player.getInventory().containsAtLeast(new ItemStack(material), count)) {
            return false;
        }

        player.getInventory().removeItem(new ItemStack(material, count));
        return true;
    }

    @Override
    public boolean sendMessage(int playerId, String message) {
        Player player = getBukkitPlayer(playerId);
        if (player == null) {
            return false;
        }

        player.sendMessage(message);
        return true;
    }

    private Player getBukkitPlayer(int playerId) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getEntityId() == playerId) {
                return player;
            }
        }
        return null;
    }

    private Material resolveMaterial(String itemId) {
        if (itemId == null || itemId.isBlank()) {
            return null;
        }

        Material material = Material.matchMaterial(itemId, true);
        if (material != null) {
            return material;
        }

        String normalized = itemId.startsWith("minecraft:")
                ? itemId.substring("minecraft:".length())
                : itemId;
        return Material.matchMaterial(normalized, true);
    }
}
