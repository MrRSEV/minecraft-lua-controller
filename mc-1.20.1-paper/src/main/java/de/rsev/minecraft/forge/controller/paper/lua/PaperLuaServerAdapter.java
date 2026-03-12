package de.rsev.minecraft.forge.controller.paper.lua;

import de.rsev.minecraft.forge.controller.lua.LuaServerAdapter;
import org.bukkit.Server;

public class PaperLuaServerAdapter implements LuaServerAdapter {

    private final Server server;

    public PaperLuaServerAdapter(Server server) {
        this.server = server;
    }

    @Override
    public Object getServer() {
        return server;
    }
}
