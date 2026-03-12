package de.rsev.minecraft.forge.controller.paper.lua.bridge.world;

import de.rsev.minecraft.forge.controller.lua.bridge.world.LuaEntityAdapter;

public class PaperLuaEntityAdapter implements LuaEntityAdapter {

    private final PaperLuaWorldAdapter worldAdapter = new PaperLuaWorldAdapter();

    @Override
    public boolean spawn(String entityId, double x, double y, double z) {
        return worldAdapter.spawnEntity(entityId, x, y, z);
    }
}
