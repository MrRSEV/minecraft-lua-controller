package de.rsev.minecraft.forge.controller.lua.bridge.io;

import org.luaj.vm2.*;
import org.tomlj.*;

import java.util.List;
import java.util.Map;

public class LuaTomlConverter {

    public static LuaTable toLua(TomlParseResult result) {
        return convertTable(result);
    }

    private static LuaTable convertTable(TomlTable toml) {

        LuaTable table = new LuaTable();

        for (String key : toml.keySet()) {
            Object value = toml.get(key);
            table.set(LuaValue.valueOf(key), convert(value));
        }

        return table;
    }

    private static LuaValue convert(Object value) {

        if (value == null) {
            return LuaValue.NIL;
        }

        // =========================
        // Primitive Types
        // =========================

        if (value instanceof String s) {
            return LuaValue.valueOf(s);
        }

        if (value instanceof Long l) {
            return LuaValue.valueOf(l);
        }

        if (value instanceof Double d) {
            return LuaValue.valueOf(d);
        }

        if (value instanceof Boolean b) {
            return LuaValue.valueOf(b);
        }

        // =========================
        // TOML Table
        // =========================

        if (value instanceof TomlTable tomlTable) {
            return convertTable(tomlTable);
        }

        // =========================
        // TOML Array
        // =========================

        if (value instanceof TomlArray tomlArray) {

            LuaTable array = new LuaTable();

            for (int i = 0; i < tomlArray.size(); i++) {
                array.set(i + 1, convert(tomlArray.get(i))); // Lua = 1-based
            }

            return array;
        }

        // =========================
        // Map fallback (extra safe)
        // =========================

        if (value instanceof Map<?, ?> map) {

            LuaTable t = new LuaTable();

            map.forEach((k, v) -> {
                LuaValue key = LuaValue.valueOf(String.valueOf(k));
                t.set(key, convert(v));
            });

            return t;
        }

        // =========================
        // List fallback
        // =========================

        if (value instanceof List<?> list) {

            LuaTable t = new LuaTable();

            int i = 1;
            for (Object v : list) {
                t.set(i++, convert(v));
            }

            return t;
        }

        // =========================
        // Unknown → string
        // =========================

        return LuaValue.valueOf(String.valueOf(value));
    }
}
