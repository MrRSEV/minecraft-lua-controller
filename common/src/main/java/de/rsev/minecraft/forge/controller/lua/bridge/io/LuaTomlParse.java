package de.rsev.minecraft.forge.controller.lua.bridge.io;

import java.nio.file.Path;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

import org.tomlj.Toml;
import org.tomlj.TomlParseResult;


import de.rsev.minecraft.forge.controller.logging.LuaLogger;

public class LuaTomlParse extends OneArgFunction {

    @Override
    public LuaValue call(LuaValue pathArg) {

        String path = pathArg.checkjstring();  // ✅ robuster als checkjstring()

        try {
            LuaLogger.debug("Toml.parse → " + path);

            Path p = Path.of(path);

            if (!java.nio.file.Files.exists(p)) {
                LuaLogger.warn("Toml.parse → file missing: " + path);
                return LuaValue.NIL;
            }

            TomlParseResult result = Toml.parse(p);

            if (result.hasErrors()) {
                LuaLogger.error("Toml.parse → errors:");
                result.errors().forEach(err ->
                        LuaLogger.error(" - " + err.toString()));
                return LuaValue.NIL;
            }

            LuaTable table = LuaTomlConverter.toLua(result);

            LuaLogger.debug("Toml.parse → success");

            return table;

        } catch (Exception e) {
            LuaLogger.error("Toml.parse failed: " + path, e);
            return LuaValue.NIL;
        }
    }

}