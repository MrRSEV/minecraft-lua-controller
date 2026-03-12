// This is an adapter, 
// to exchange data from different forge repos to the common repos

package de.rsev.minecraft.forge.controller.lua.bridge.world;

import org.luaj.vm2.LuaTable;

public interface LuaWorldAdapter {

    // Blocks
    boolean setBlock(int x, int y, int z, String blockId);
    String getBlock(int x, int y, int z);
    boolean setSignText(int x, int y, int z, String l1, String l2);

    // Entities
    boolean spawnEntity(String id, double x, double y, double z);
    LuaTable getAllEntities();
    LuaTable getEntities(String type, double x, double y, double z, double range);
    boolean removeEntity(int id);
    int killAllEntities();
    int killEntities(LuaTable filter);

    // Players
    LuaTable getPlayers();
    Object getPlayerById(int id);

    // Time
    long getWorldTime();
    boolean setWorldTime(long time);

    // Weather
    String getWeather();
    boolean setWeather(String type);
}