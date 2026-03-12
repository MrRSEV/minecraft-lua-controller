package de.rsev.minecraft.forge.controller.lua.bridge.config;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import org.luaj.vm2.*;

import java.nio.file.*;

public class LuaConfigBridgeInternal {

    public static LuaValue load(String path) {

        LuaLogger.info("Lua → Config.load '{}'", path);

        try {
            String content = Files.readString(Path.of(path));
            LuaValue chunk = new Globals().load("return " + content);
            return chunk.call();
        } catch (Exception e) {
            LuaLogger.error("Config.load failed", e);
            return LuaValue.NIL;
        }
    }

    public static boolean save(String path, LuaTable table) {

        LuaLogger.info("Lua → Config.save '{}'", path);

        try {
            Files.writeString(Path.of(path), table.toString());
            return true;
        } catch (Exception e) {
            LuaLogger.error("Config.save failed", e);
            return false;
        }
    }
}
