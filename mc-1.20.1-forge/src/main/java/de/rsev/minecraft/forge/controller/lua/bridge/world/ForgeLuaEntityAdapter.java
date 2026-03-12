// This is an adapter, 
// to exchange data from different forge repos to the common repos

package de.rsev.minecraft.forge.controller.lua.bridge.world;

public class ForgeLuaEntityAdapter implements LuaEntityAdapter {

    @Override
    public boolean spawn(String entityId, double x, double y, double z) {
        return LuaWorldBridgeInternal.spawnEntity(entityId, x, y, z);
    }
}