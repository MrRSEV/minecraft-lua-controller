// This is an adapter, 
// to exchange data from different forge repos to the common repos

package de.rsev.minecraft.forge.controller.lua.bridge.world;

public interface LuaEntityAdapter {

    boolean spawn(String entityId, double x, double y, double z);

}