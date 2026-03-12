package de.rsev.minecraft.forge.controller.lua.script;

import de.rsev.minecraft.forge.controller.lua.LuaRuntime;
import de.rsev.minecraft.forge.controller.lua.bridge.LuaBridgeInjector;
import de.rsev.minecraft.forge.controller.lua.sandbox.LuaSandbox;
import de.rsev.minecraft.forge.controller.lua.vm.LuaVM;
import de.rsev.minecraft.forge.controller.lua.vm.LuaVMFactory;

import java.nio.file.Path;

public class LuaScriptLoader {

    public static LuaScriptContainer load(Path scriptPath) {
        
        Path worldDir = LuaRuntime.getCurrentWorldDir();
        LuaVM vm = LuaVMFactory.create(worldDir);

        LuaSandbox.apply(vm);
        LuaBridgeInjector.inject(vm);

        LuaScriptContainer container = new LuaScriptContainer(scriptPath, vm);

        vm.callIfExists("onLoad");
        container.execute();
        vm.callIfExists("onEnable");

        // Registrierung erfolgt ausschließlich im LuaRuntime

        return container;
    }

    private LuaScriptLoader() {}
}
