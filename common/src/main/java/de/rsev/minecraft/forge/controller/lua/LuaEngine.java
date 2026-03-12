package de.rsev.minecraft.forge.controller.lua;

import java.nio.file.Path;

import de.rsev.minecraft.forge.controller.lua.vm.LuaVM;
import de.rsev.minecraft.forge.controller.lua.vm.LuaVMFactory;

public class LuaEngine {

    public LuaVM createVM(Path worldDir) {
        return LuaVMFactory.create(worldDir);
    }
}
