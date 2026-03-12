package de.rsev.minecraft.forge.controller.lua.bridge.world;

public class LuaWorldAPI {

    private final LuaBlockAPI blocks = new LuaBlockAPI();
    private final LuaEntityAPI entities = new LuaEntityAPI();

    public LuaBlockAPI blocks() {
        return blocks;
    }

    public LuaEntityAPI entities() {
        return entities;
    }

    
}
