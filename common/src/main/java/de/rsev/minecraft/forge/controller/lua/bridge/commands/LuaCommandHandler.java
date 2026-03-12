package de.rsev.minecraft.forge.controller.lua.bridge.commands;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

import de.rsev.minecraft.forge.controller.lua.LuaRuntime;

/**
 * LuaCommandHandler
 *
 * Plattformunabhängiger Handler für Lua Commands.
 * Wird von der Plattform (Forge) aufgerufen und erhält
 * bereits vorbereitete Wrapper-Objekte.
 */
public class LuaCommandHandler {

    private final LuaFunction function;

    public LuaCommandHandler(LuaFunction function) {
        this.function = function;
    }

    public void execute(Object playerWrapper, String[] args) {

        LuaTable ctx = LuaCommandContextAdapter.adapt(
                new LuaRuntime.CommandContext(playerWrapper, args)
        );

        function.call(ctx);
    }
}