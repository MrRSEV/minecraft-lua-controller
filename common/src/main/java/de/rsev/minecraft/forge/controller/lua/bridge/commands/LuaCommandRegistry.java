package de.rsev.minecraft.forge.controller.lua.bridge.commands;

import org.luaj.vm2.LuaFunction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LuaCommandRegistry {

    private static final Map<String, LuaFunction> COMMANDS = new ConcurrentHashMap<>();

    public static void register(String name, LuaFunction function) {
        if (name == null || name.isBlank() || function == null) return;
        COMMANDS.put(name, function);
    }

    public static LuaFunction get(String name) {
        return COMMANDS.get(name);
    }

    public static Map<String, LuaFunction> getAll() {
        return Map.copyOf(COMMANDS);
    }

    public static void unregister(String name) {
        COMMANDS.remove(name);
    }

    private LuaCommandRegistry() {}
}
