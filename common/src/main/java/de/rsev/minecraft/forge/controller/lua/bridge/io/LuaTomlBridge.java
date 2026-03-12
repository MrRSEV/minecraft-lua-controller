package de.rsev.minecraft.forge.controller.lua.bridge.io;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.Varargs;

import java.nio.file.Files;
import java.nio.file.Path;

public class LuaTomlBridge extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue pathArg, LuaValue tableArg) {

        String path = pathArg.checkjstring();

        if (!tableArg.istable()) {
            LuaLogger.error("Toml.write → table expected, got " + tableArg.typename());
            return LuaValue.FALSE;
        }

        LuaTable table = (LuaTable) tableArg;

        try {
            LuaLogger.debug("Toml.write → " + path);

            Path p = Path.of(path);
            Files.createDirectories(p.getParent());

            String toml = toToml(table, "");

            Files.writeString(p, toml);

            LuaLogger.info("Toml.write → success: " + p.getFileName());
            return LuaValue.TRUE;

        } catch (Exception e) {
            LuaLogger.error("Toml.write failed: " + path, e);
            return LuaValue.FALSE;
        }
    }

    // =========================
    // LuaTable → TOML
    // =========================
    private String toToml(LuaTable table, String prefix) {

        StringBuilder sb = new StringBuilder();

        LuaValue k = LuaValue.NIL;

        while (true) {
            Varargs n = table.next(k);
            if ((k = n.arg1()).isnil()) break;

            LuaValue v = n.arg(2);

            String key = k.checkjstring();

            if (v.istable()) {
                LuaTable sub = (LuaTable) v;

                sb.append("\n[").append(prefix).append(key).append("]\n");
                sb.append(toToml(sub, prefix + key + "."));

            } else {
                sb.append(key)
                  .append(" = ")
                  .append(formatValue(v))
                  .append("\n");
            }
        }

        return sb.toString();
    }

    private String formatValue(LuaValue v) {

        if (v.isstring()) {
            return "\"" + v.tojstring() + "\"";
        }

        if (v.isboolean()) {
            return v.toboolean() ? "true" : "false";
        }

        if (v.isnumber()) {
            return v.tojstring();
        }

        return "\"" + v.tojstring() + "\"";
    }
}
