package de.rsev.minecraft.forge.controller.lua.bridge.io;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.TwoArgFunction;
import org.json.*;

import java.nio.file.Files;
import java.nio.file.Path;

public class LuaJsonWriteFunction extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue pathArg, LuaValue tableArg) {

        String path = pathArg.checkjstring();

        if (!tableArg.istable()) {
            return LuaValue.FALSE;
        }

        try {
            JSONObject json = LuaJsonConverter.toJson(tableArg.checktable());

            Path p = Path.of(path);
            Files.createDirectories(p.getParent());

            Files.writeString(p, json.toString(4)); // pretty print

            return LuaValue.TRUE;

        } catch (Exception e) {
            return LuaValue.FALSE;
        }
    }
}