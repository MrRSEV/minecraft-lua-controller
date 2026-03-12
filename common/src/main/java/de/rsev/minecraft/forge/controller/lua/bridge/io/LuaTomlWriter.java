package de.rsev.minecraft.forge.controller.lua.bridge.io;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public class LuaTomlWriter {

    public static String toToml(LuaTable table) {
        StringBuilder sb = new StringBuilder();
        writeTable(sb, table, "");
        return sb.toString();
    }

    private static void writeTable(StringBuilder sb, LuaTable table, String prefix) {

        LuaValue key = LuaValue.NIL;

        while (true) {
            Varargs n = table.next(key);
            if ((key = n.arg1()).isnil()) break;

            LuaValue value = n.arg(2);

            String name = key.tojstring();

            if (value.istable()) {
                sb.append("\n[").append(name).append("]\n");
                writeTable(sb, (LuaTable) value, name + ".");
            } else {
                sb.append(name)
                  .append(" = ")
                  .append(format(value))
                  .append("\n");
            }
        }
    }

    private static String format(LuaValue value) {

        if (value.isstring()) {
            return "\"" + value.tojstring() + "\"";
        }

        if (value.isboolean()) {
            return value.toboolean() ? "true" : "false";
        }

        if (value.isnumber()) {
            return value.tojstring();
        }

        return "\"" + value.tojstring() + "\"";
    }
}
