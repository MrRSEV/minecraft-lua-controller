// This is an adapter, 
// to exchange data from different forge repos to the common repos

package de.rsev.minecraft.forge.controller.lua.bridge.inventory;

public class ForgeLuaInventoryAdapter implements LuaInventoryAdapter {

    @Override
    public boolean addItem(int playerId, String itemId, int count) {
        return LuaInventoryBridgeInternal.addItem(playerId, itemId, count);
    }

    @Override
    public boolean removeItem(int playerId, String itemId, int count) {
        return LuaInventoryBridgeInternal.removeItem(playerId, itemId, count);
    }

    @Override
    public Object getInventory(int playerId) {
        return LuaInventoryBridgeInternal.getInventory(playerId);
    }

    @Override
    public boolean addItemToBlock(int x, int y, int z, String itemId, int count) {
        return LuaInventoryBridgeInternal.addItemToBlock(x, y, z, itemId, count);
    }
}