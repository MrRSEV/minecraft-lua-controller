package de.rsev.minecraft.forge.controller.lua.bridge.inventory;

public interface LuaInventoryAdapter {

    boolean addItem(int playerId, String itemId, int count);

    boolean removeItem(int playerId, String itemId, int count);

    Object getInventory(int playerId);

    boolean addItemToBlock(int x, int y, int z, String itemId, int count);
}