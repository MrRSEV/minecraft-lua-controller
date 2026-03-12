package de.rsev.minecraft.forge.controller.lua.bridge.system;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import de.rsev.minecraft.forge.controller.lua.LuaRuntime;

public class LuaSystemBridge {

    public static LuaTable create() {

        LuaTable system = new LuaTable();

        // =========================
        // print
        // =========================
        system.set("print", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue message) {

                String text = message.isnil() ? "nil" : message.tojstring();

                LuaLogger.info("[LUA] " + text);

                if (LuaRuntime.isDevMode()) {
                    System.out.println("[LUA][INFO] " + text);
                }

                return LuaValue.NIL;
            }
        });

        // =========================
        // warn
        // =========================
        system.set("warn", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue message) {

                String text = message.isnil() ? "nil" : message.tojstring();

                LuaLogger.warn("[LUA] " + text);

                if (LuaRuntime.isDevMode()) {
                    System.out.println("[LUA][WARN] " + text);
                }

                return LuaValue.NIL;
            }
        });

        // =========================
        // error
        // =========================
        system.set("error", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue message) {

                String text = message.isnil() ? "nil" : message.tojstring();

                LuaLogger.error("[LUA] " + text);

                if (LuaRuntime.isDevMode()) {
                    System.err.println("[LUA][ERROR] " + text);
                }

                return LuaValue.NIL;
            }
        });

        // =========================
        // debug
        // =========================
        system.set("debug", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue message) {

                String text = message.isnil() ? "nil" : message.tojstring();

                LuaLogger.debug("[LUA] " + text);

                if (LuaRuntime.isDevMode()) {
                    System.out.println("[LUA][DEBUG] " + text);
                }

                return LuaValue.NIL;
            }
        });

        // =========================
        // currentTimeMillis
        // =========================
        system.set("currentTimeMillis", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(System.currentTimeMillis());
            }
        });

        return system;
    }
}
