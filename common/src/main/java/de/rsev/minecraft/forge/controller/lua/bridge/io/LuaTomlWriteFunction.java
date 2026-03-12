package de.rsev.minecraft.forge.controller.lua.bridge.io;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.TwoArgFunction;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;

import java.nio.file.Files;
import java.nio.file.Path;

public class LuaTomlWriteFunction extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue pathArg, LuaValue tableArg) {

        String path = pathArg.checkjstring();

        if (!tableArg.istable()) {
            LuaLogger.error("Toml.write → table expected");
            return LuaValue.FALSE;
        }

        LuaTable table = tableArg.checktable();

        try {
            LuaLogger.debug("Toml.write → " + path);

            Path p = Path.of(path);
            Files.createDirectories(p.getParent());

            String toml = LuaTomlWriter.toToml(table);
            Files.writeString(p, toml);

            LuaLogger.info("Toml.write → success");

            return LuaValue.TRUE;

        } catch (Exception e) {
            LuaLogger.error("Toml.write failed: " + path, e);
            return LuaValue.FALSE;
        }
    }
}
