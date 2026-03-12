package de.rsev.minecraft.forge.controller.lua.vm.types;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import de.rsev.minecraft.forge.controller.lua.vm.LuaVM;
import de.rsev.minecraft.forge.controller.lua.vm.LuaVMFactory;

import java.nio.file.Path;

public class LoadingVM {

    public void executeScript(Path script) {
        LuaVM vm = LuaVMFactory.create(null);

        try {
            vm.execute(script);
            vm.callIfExists("shutdown");
        } catch (Exception e) {
            LuaLogger.error("Loading VM failed for " + script.getFileName(), e);
        }
    }
}
