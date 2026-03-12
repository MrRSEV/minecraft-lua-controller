// This is an adapter, 
// to exchange data from different forge repos to the common repos

package de.rsev.minecraft.forge.controller.lua;

import net.minecraft.server.MinecraftServer;

public class ForgeLuaServerAdapter implements LuaServerAdapter {

    private final MinecraftServer server;

    public ForgeLuaServerAdapter(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public Object getServer() {
        return server;
    }
}