package de.rsev.minecraft.forge.controller.lua.bridge.io;

import java.nio.file.Files;
import java.nio.file.Path;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;

public class LuaFileBridge {

    public static LuaTable create() {

        LuaTable file = new LuaTable();

        // =========================
        // exists(path)
        // =========================
        file.set("exists", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue pathArg) {

                String path = pathArg.checkjstring();

                try {
                    return LuaValue.valueOf(Files.exists(Path.of(path)));

                } catch (Exception e) {
                    System.err.println("[LUA][File.exists] " + e.getMessage());
                    LuaLogger.error("[LUA][File.exists] " + e.getMessage());
                    return LuaValue.FALSE;
                }
            }
        });

        // =========================
        // write(path, content)
        // =========================
        file.set("write", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue pathArg, LuaValue contentArg) {

                String path = pathArg.checkjstring();
                String content = contentArg.checkjstring();

                try {
                    Path p = LuaPathSandbox.resolve(path);

                    // ✅ Parent-Verzeichnisse sicher erstellen
                    if (p.getParent() != null) {
                        Files.createDirectories(p.getParent());
                    }

                    Files.writeString(p, content);

                    return LuaValue.TRUE;

                } catch (Exception e) {
                    System.err.println("[LUA][File.write] " + e.getMessage());
                    LuaLogger.error("[LUA][File.write] " + e.getMessage());
                    return LuaValue.FALSE;
                }
            }
        });

        // =========================
        // read(path)
        // =========================
        file.set("read", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue pathArg) {

                String path = pathArg.checkjstring();

                try {
                    String content = Files.readString(Path.of(path));
                    return LuaValue.valueOf(content);

                } catch (Exception e) {
                    System.err.println("[LUA][File.read] " + e.getMessage());
                    LuaLogger.error("[LUA][File.read] " + e.getMessage());
                    return LuaValue.NIL;
                }
            }
        });

        return file;
    }

    // =========================
    // Java Helper API
    // =========================

    public boolean exists(String path) {
        try {
            return Files.exists(Path.of(path));

        } catch (Exception e) {
            System.err.println("[LUA][File.exists] " + e.getMessage());
            LuaLogger.error("[LUA][File.exists] " + e.getMessage());
            return false;
        }
    }

    public boolean write(String path, String content) {
        try {
            Path p = LuaPathSandbox.resolve(path);

            // ✅ Null-safe Directory Creation
            if (p.getParent() != null) {
                Files.createDirectories(p.getParent());
            }

            Files.writeString(p, content);

            return true;

        } catch (Exception e) {
            System.err.println("[LUA][File.write] " + e.getMessage());
            LuaLogger.error("[LUA][File.write] " + e.getMessage());
            return false;
        }
    }

    public String read(String path) {
        try {
            return Files.readString(Path.of(path));

        } catch (Exception e) {
            System.err.println("[LUA][File.read] " + e.getMessage());
            LuaLogger.error("[LUA][File.read] " + e.getMessage());
            return null;
        }
    }
}