// This is an adapter, 
// to exchange data from different forge repos to the common repos

package de.rsev.minecraft.forge.controller.lua.bridge.scoreboard;

import org.luaj.vm2.LuaValue;

public class ForgeLuaScoreboardAdapter implements LuaScoreboardAdapter {

    @Override
    public boolean setScore(String objective, int playerId, int value) {
        return LuaScoreboardBridgeInternal.setScore(objective, playerId, value);
    }

    @Override
    public boolean addScore(String objective, int playerId, int value) {
        return LuaScoreboardBridgeInternal.addScore(objective, playerId, value);
    }

    @Override
    public int getScore(String objective, int playerId) {
        return LuaScoreboardBridgeInternal.getScore(objective, playerId);
    }

    @Override
    public LuaValue listObjectives() {
        return LuaScoreboardBridgeInternal.listObjectives();
    }
}