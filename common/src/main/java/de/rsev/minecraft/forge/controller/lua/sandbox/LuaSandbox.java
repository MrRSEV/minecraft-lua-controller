package de.rsev.minecraft.forge.controller.lua.sandbox;

import de.rsev.minecraft.forge.controller.lua.vm.LuaVM;

public class LuaSandbox {

    public static void apply(LuaVM vm) {
        //vm.remove("os");
        //vm.remove("debug");

        // ⚠ io & package NICHT entfernen → Lua braucht sie
        // vm.remove("io");
        // vm.remove("package");
    }

    private LuaSandbox() {}
}
