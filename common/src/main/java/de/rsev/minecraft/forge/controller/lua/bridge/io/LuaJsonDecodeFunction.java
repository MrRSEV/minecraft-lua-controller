package de.rsev.minecraft.forge.controller.lua.bridge.io;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.json.*;

public class LuaJsonDecodeFunction extends OneArgFunction {

    @Override
    public LuaValue call(LuaValue jsonArg) {

        if (!jsonArg.isstring()) {
            return LuaValue.error("Json.decode → string expected");
        }

        try {
            String jsonString = jsonArg.tojstring();

            Object parsed;

            if (jsonString.trim().startsWith("[")) {
                parsed = new JSONArray(jsonString);
            } else {
                parsed = new JSONObject(jsonString);
            }

            return LuaJsonConverter.toLua(parsed);

        } catch (Exception e) {
            return LuaValue.NIL;
        }
    }
}