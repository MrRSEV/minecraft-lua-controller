// This is an adapter, 
// to exchange data from different forge repos to the common repos

package de.rsev.minecraft.forge.controller.lua.bridge.scoreboard;

import org.luaj.vm2.LuaValue;

public interface LuaScoreboardAdapter {

    boolean setScore(String objective, int playerId, int value);

    boolean addScore(String objective, int playerId, int value);

    int getScore(String objective, int playerId);

    LuaValue listObjectives();

}