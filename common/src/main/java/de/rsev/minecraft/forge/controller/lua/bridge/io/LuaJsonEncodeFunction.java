package de.rsev.minecraft.forge.controller.lua.bridge.io;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.json.JSONObject;

public class LuaJsonEncodeFunction extends OneArgFunction {

    @Override
    public LuaValue call(LuaValue value) {

        try {
            Object javaObj = LuaJsonConverter.toJava(value);

            JSONObject json = new JSONObject(javaObj);

            return LuaValue.valueOf(json.toString(4)); // pretty print

        } catch (Exception e) {
            return LuaValue.NIL;
        }
    }
}