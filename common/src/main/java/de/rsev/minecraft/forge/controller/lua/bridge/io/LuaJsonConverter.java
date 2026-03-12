package de.rsev.minecraft.forge.controller.lua.bridge.io;

import org.luaj.vm2.*;
import org.json.*; 

public class LuaJsonConverter {

    // JSON → Lua
    public static LuaValue toLua(Object obj) {

        if (obj == null) return LuaValue.NIL;

        if (obj instanceof JSONObject jsonObj) {

            LuaTable table = new LuaTable();

            for (String key : jsonObj.keySet()) {
                table.set(key, toLua(jsonObj.get(key)));
            }

            return table;
        }

        if (obj instanceof JSONArray array) {

            LuaTable table = new LuaTable();

            for (int i = 0; i < array.length(); i++) {
                table.set(i + 1, toLua(array.get(i)));
            }

            return table;
        }

        if (obj instanceof Boolean b) return LuaValue.valueOf(b);
        if (obj instanceof Number n) return LuaValue.valueOf(n.doubleValue());
        if (obj instanceof String s) return LuaValue.valueOf(s);

        return LuaValue.valueOf(obj.toString());
    }

    // Lua → JSON
    public static JSONObject toJson(LuaTable table) {

        JSONObject json = new JSONObject();

        LuaValue k = LuaValue.NIL;

        while (true) {
            Varargs n = table.next(k);
            if ((k = n.arg1()).isnil()) break;

            LuaValue v = n.arg(2);

            json.put(k.tojstring(), toJava(v));
        }

        return json;
    }

    public static Object toJava(LuaValue value) {

        if (value.isnil()) return JSONObject.NULL;
        if (value.isboolean()) return value.toboolean();
        if (value.isnumber()) return value.todouble();
        if (value.isstring()) return value.tojstring();

        if (value.istable()) {

            LuaTable table = value.checktable();

            // Prüfen ob Array-artig
            //boolean isArray = true;
            int index = 1;

            while (true) {
                LuaValue v = table.rawget(index++);
                if (v.isnil()) break;
            }

            // Wenn Index 1 existiert → Array
            if (!table.rawget(1).isnil()) {

                JSONArray array = new JSONArray();

                index = 1;
                while (true) {
                    LuaValue v = table.rawget(index++);
                    if (v.isnil()) break;

                    array.put(toJava(v));
                }

                return array;
            }

            // Sonst Object
            JSONObject obj = new JSONObject();

            LuaValue k = LuaValue.NIL;

            while (true) {
                Varargs n = table.next(k);
                if ((k = n.arg1()).isnil()) break;

                LuaValue v = n.arg(2);

                obj.put(k.tojstring(), toJava(v));
            }

            return obj;
        }

        return value.tojstring();
    }
    
}