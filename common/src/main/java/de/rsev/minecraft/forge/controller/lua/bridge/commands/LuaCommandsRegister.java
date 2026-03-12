package de.rsev.minecraft.forge.controller.lua.bridge.commands;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import de.rsev.minecraft.forge.controller.lua.LuaRuntime;
import de.rsev.minecraft.forge.controller.logging.LuaLogger;

public class LuaCommandsRegister extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue nameArg, LuaValue funcArg) {

        String name = nameArg.checkjstring();

        if (!funcArg.isfunction()) {
            LuaLogger.error("Commands.register → second arg not function");
            return NIL;
        }

        LuaFunction function = (LuaFunction) funcArg;

        LuaRuntime.registerLuaCommand(name, new LuaCommandHandler(function));

        LuaLogger.info("Lua command registered → {}", name);

        return NIL;
    }
}
