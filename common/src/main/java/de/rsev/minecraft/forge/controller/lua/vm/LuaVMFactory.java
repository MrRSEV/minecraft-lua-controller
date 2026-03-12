package de.rsev.minecraft.forge.controller.lua.vm;

import java.nio.file.Path;

public class LuaVMFactory {

    public static LuaVM create(Path worldDir) {
        return new LuaVM(worldDir);
    }

    private LuaVMFactory() {}
}
