package de.rsev.minecraft.forge.controller.lua.vm.types;

import de.rsev.minecraft.forge.controller.lua.vm.LuaVM;

public class EventsVM {

    private final LuaVM vm;

    public EventsVM(LuaVM vm) {
        this.vm = vm;
    }

    public void executeScript(java.nio.file.Path script) {
        vm.execute(script);
    }

    public void tick() {
        vm.callIfExists("onTick");
    }

    public LuaVM getVm() {
        return vm;
    }
}
