package de.rsev.minecraft.forge.controller.lua.bridge;

import de.rsev.minecraft.forge.controller.lua.vm.LuaVM;

import de.rsev.minecraft.forge.controller.lua.bridge.commands.LuaCommandsBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.world.LuaWorldBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.inventory.LuaInventoryBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.system.LuaSystemBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.timer.LuaTimerBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.config.LuaConfigBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.events.LuaEventsBridge;

public class LuaBridgeInjector {

    public static void inject(LuaVM vm) {

        vm.set("Events", LuaEventsBridge.create());
        vm.set("Commands", LuaCommandsBridge.create());
        vm.set("Player", LuaPlayerBridge.create());
        vm.set("World", LuaWorldBridge.create());
        vm.set("Inventory", LuaInventoryBridge.create());
        vm.set("Timer", LuaTimerBridge.create());
        vm.set("Config", LuaConfigBridge.create());
        vm.set("System", LuaSystemBridge.create());
    }

    private LuaBridgeInjector() {}
}
