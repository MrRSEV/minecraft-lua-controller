package de.rsev.minecraft.forge.controller.lua.bridge.commands;

import de.rsev.minecraft.forge.controller.lua.LuaRuntime;
import de.rsev.minecraft.forge.controller.lua.vm.LuaVMManager;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class LuaCommandsBridge {

    public static LuaTable create() {

        LuaTable table = new LuaTable();

        table.set("register", new RegisterFunction());
        table.set("unregister", new UnregisterFunction());

        return table;
    }

    // =========================
    // Commands.register(name, func)
    // =========================

    private static class RegisterFunction extends VarArgFunction {

        @Override
        public Varargs invoke(Varargs args) {

            String name = args.arg(1).checkjstring();
            String description;
            LuaFunction func;

            if (args.narg() == 2) {
                // Commands.register(name, func)
                description = "Lua Command";
                func = args.arg(2).checkfunction();
            }
            else {
                // Commands.register(name, description, func)
                description = args.arg(2).optjstring("Lua Command");
                func = args.arg(3).checkfunction();
            }

            LuaCommandHandler handler = new LuaCommandHandler(func);

            LuaCommandRegistry.register(name, func);
            LuaRuntime.registerLuaCommand(name, handler);
            LuaVMManager.setCommandDescription(name, description);

            return LuaValue.NIL;
        }
    }


    // =========================
    // Commands.unregister(name)
    // =========================
    private static class UnregisterFunction extends OneArgFunction {

        @Override
        public LuaValue call(LuaValue name) {

            if (!name.isstring()) {
                return LuaValue.error("Commands.unregister → name must be string");
            }

            String commandName = name.tojstring();

            LuaCommandRegistry.unregister(commandName);
            LuaRuntime.unregisterLuaCommand(commandName);

            return LuaValue.NIL;
        }
    }

    private LuaCommandsBridge() {}
}
