package de.rsev.minecraft.forge.controller.lua.bridge.io;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.TwoArgFunction;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Files;
import java.nio.file.Path;

public class LuaYamlWriteFunction extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue pathArg, LuaValue tableArg) {

        String path = pathArg.checkjstring();

        if (!tableArg.istable()) return LuaValue.FALSE;

        try {
            Object javaObj = LuaYamlConverter.toJava(tableArg.checktable());

            Yaml yaml = new Yaml();
            String output = yaml.dump(javaObj);

            Path p = Path.of(path);
            Files.createDirectories(p.getParent());

            Files.writeString(p, output);

            return LuaValue.TRUE;

        } catch (Exception e) {
            return LuaValue.FALSE;
        }
    }
}