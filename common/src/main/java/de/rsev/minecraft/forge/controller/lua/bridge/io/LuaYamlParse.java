package de.rsev.minecraft.forge.controller.lua.bridge.io;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Files;
import java.nio.file.Path;

public class LuaYamlParse extends OneArgFunction {

    @Override
    public LuaValue call(LuaValue pathArg) {

        String path = pathArg.checkjstring();

        try {
            String content = Files.readString(Path.of(path));

            Yaml yaml = new Yaml();
            Object data = yaml.load(content);

            return LuaYamlConverter.toLua(data);

        } catch (Exception e) {
            return LuaValue.NIL;
        }
    }
}