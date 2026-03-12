package de.rsev.minecraft.forge.controller.paper.lua.bridge.inventory;

import de.rsev.minecraft.forge.controller.lua.bridge.inventory.LuaInventoryAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PaperLuaInventoryAdapter implements LuaInventoryAdapter {

    @Override
    public boolean addItem(int playerId, String itemId, int count) {
        Player player = getPlayer(playerId);
        Material material = resolveMaterial(itemId);
        if (player == null || material == null) {
            return false;
        }

        player.getInventory().addItem(new ItemStack(material, count));
        return true;
    }

    @Override
    public boolean removeItem(int playerId, String itemId, int count) {
        Player player = getPlayer(playerId);
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
    public Object getInventory(int playerId) {
        Player player = getPlayer(playerId);
        return player != null ? player.getInventory() : null;
    }

    @Override
    public boolean addItemToBlock(int x, int y, int z, String itemId, int count) {
        Material material = resolveMaterial(itemId);
        if (material == null || Bukkit.getWorlds().isEmpty()) {
            return false;
        }

        BlockState state = Bukkit.getWorlds().get(0).getBlockAt(new Location(Bukkit.getWorlds().get(0), x, y, z)).getState();
        if (!(state instanceof Container container)) {
            return false;
        }

        container.getInventory().addItem(new ItemStack(material, count));
        return true;
    }

    private Player getPlayer(int playerId) {
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
