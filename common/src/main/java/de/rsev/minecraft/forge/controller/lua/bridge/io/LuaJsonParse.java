package de.rsev.minecraft.forge.controller.lua.bridge.io;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.json.*;

import java.nio.file.Files;
import java.nio.file.Path;

public class LuaJsonParse extends OneArgFunction {

    @Override
    public LuaValue call(LuaValue pathArg) {

        String path = pathArg.checkjstring();

        try {
            String content = Files.readString(Path.of(path));

            JSONObject json = new JSONObject(content);

            return LuaJsonConverter.toLua(json);

        } catch (Exception e) {
            return LuaValue.NIL;
        }
    }
}