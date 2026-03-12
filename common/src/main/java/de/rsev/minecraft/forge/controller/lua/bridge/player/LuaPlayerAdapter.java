// This is an adapter, 
// to exchange data from different forge repos to the common repos

package de.rsev.minecraft.forge.controller.lua.bridge.player;

import org.luaj.vm2.LuaValue;

public interface LuaPlayerAdapter {

    LuaPlayerWrapper getPlayer(int playerId);

    LuaValue getAllPlayers();

    boolean addItem(int playerId, String itemId, int count);

    boolean removeItem(int playerId, String itemId, int count);

    boolean sendMessage(int playerId, String message);

}