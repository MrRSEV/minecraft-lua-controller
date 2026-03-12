package de.rsev.minecraft.forge.controller.lua.vm.types;

import de.rsev.minecraft.forge.controller.lua.vm.LuaVM;

public class CommandsVM {

    private final LuaVM vm;

    public CommandsVM(LuaVM vm) {
        this.vm = vm;
    }

    public void executeScript(java.nio.file.Path script) {
        vm.execute(script);
    }

    public LuaVM getVm() {
        return vm;
    }
}
