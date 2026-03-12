package de.rsev.minecraft.forge.controller.lua.bridge.io;

import org.luaj.vm2.*;

import java.util.*;

public class LuaYamlConverter {

    public static LuaValue toLua(Object obj) {

        if (obj == null) return LuaValue.NIL;

        if (obj instanceof Map<?, ?> map) {

            LuaTable table = new LuaTable();

            map.forEach((k, v) ->
                    table.set(String.valueOf(k), toLua(v)));

            return table;
        }

        if (obj instanceof List<?> list) {

            LuaTable table = new LuaTable();

            int i = 1;
            for (Object v : list) {
                table.set(i++, toLua(v));
            }

            return table;
        }

        if (obj instanceof Boolean b) return LuaValue.valueOf(b);
        if (obj instanceof Number n) return LuaValue.valueOf(n.doubleValue());
        if (obj instanceof String s) return LuaValue.valueOf(s);

        return LuaValue.valueOf(obj.toString());
    }

    public static Object toJava(LuaTable table) {

        Map<String, Object> map = new LinkedHashMap<>();

        LuaValue k = LuaValue.NIL;

        while (true) {
            Varargs n = table.next(k);
            if ((k = n.arg1()).isnil()) break;

            LuaValue v = n.arg(2);

            map.put(k.tojstring(), convert(v));
        }

        return map;
    }

    private static Object convert(LuaValue v) {

        if (v.isnil()) return null;
        if (v.isboolean()) return v.toboolean();
        if (v.isnumber()) return v.todouble();
        if (v.isstring()) return v.tojstring();
        if (v.istable()) return toJava(v.checktable());

        return v.tojstring();
    }
}