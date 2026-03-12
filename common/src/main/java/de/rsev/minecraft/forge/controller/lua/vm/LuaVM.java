package de.rsev.minecraft.forge.controller.lua.vm;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import de.rsev.minecraft.forge.controller.lua.LuaRuntime;
import de.rsev.minecraft.forge.controller.lua.bridge.commands.LuaCommandsBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.assets.LuaAssetsBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.inventory.LuaInventoryBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.io.LuaFileBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.io.LuaJsonDecodeFunction;
import de.rsev.minecraft.forge.controller.lua.bridge.io.LuaJsonEncodeFunction;
import de.rsev.minecraft.forge.controller.lua.bridge.io.LuaJsonParse;
import de.rsev.minecraft.forge.controller.lua.bridge.io.LuaJsonWriteFunction;
import de.rsev.minecraft.forge.controller.lua.bridge.io.LuaTomlParse;
import de.rsev.minecraft.forge.controller.lua.bridge.io.LuaTomlWriteFunction;
import de.rsev.minecraft.forge.controller.lua.bridge.io.LuaYamlParse;
import de.rsev.minecraft.forge.controller.lua.bridge.io.LuaYamlWriteFunction;
import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.scoreboard.LuaScoreboardBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.system.LuaSystemBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.timer.LuaTimerBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.world.LuaWorldBridge;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.BaseLib;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.DebugLib;
import org.luaj.vm2.lib.MathLib;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LuaVM {

    private Globals globals;
    private final Path worldDir;
    private final Map<Path, LuaValue> requiredModules = new HashMap<>();

    public LuaVM(Path worldDir) {
        this.worldDir = worldDir;
        this.globals = createSandboxGlobals(worldDir);
    }

    private Globals createSandboxGlobals(Path worldDir) {
        Globals g = new Globals();

        try {
            LuaC.install(g);

            g.load(new BaseLib());
            g.load(new PackageLib());
            g.load(new MathLib());
            g.load(new StringLib());
            g.load(new TableLib());
            g.load(new Bit32Lib());
            g.load(new DebugLib());

            g.set("System", LuaSystemBridge.create());
            g.set("File", LuaFileBridge.create());
            g.set("Player", LuaPlayerBridge.create());
            g.set("World", LuaWorldBridge.create());
            g.set("Commands", LuaCommandsBridge.create());
            g.set("Timer", LuaTimerBridge.create());
            g.set("Scoreboard", LuaScoreboardBridge.create());
            g.set("Inventory", LuaInventoryBridge.create());
            g.set("Assets", LuaAssetsBridge.create());

            LuaTable toml = new LuaTable();
            toml.set("parse", new LuaTomlParse());
            toml.set("write", new LuaTomlWriteFunction());
            g.set("Toml", toml);

            LuaTable json = new LuaTable();
            json.set("parse", new LuaJsonParse());
            json.set("write", new LuaJsonWriteFunction());
            json.set("encode", new LuaJsonEncodeFunction());
            json.set("decode", new LuaJsonDecodeFunction());
            g.set("Json", json);

            LuaTable yaml = new LuaTable();
            yaml.set("parse", new LuaYamlParse());
            yaml.set("write", new LuaYamlWriteFunction());
            g.set("Yaml", yaml);

            g.set("io", LuaValue.NIL);
            g.set("os", LuaValue.NIL);

            if (worldDir != null) {
                g.set("WORLD_DIR", LuaValue.valueOf(worldDir.toString()));
            }

            if (LuaRuntime.getLuaWorldDir() != null) {
                g.set("LUA_WORLD_DIR", LuaValue.valueOf(LuaRuntime.getLuaWorldDir().toString()));
            }

            if (LuaRuntime.getLuaGlobalDir() != null) {
                g.set("LUA_GLOBAL_DIR", LuaValue.valueOf(LuaRuntime.getLuaGlobalDir().toString()));
            }

            if (LuaRuntime.getLuaLibsDir() != null) {
                Path libsDir = LuaRuntime.getLuaLibsDir();
                g.set("LUA_LIBS_DIR", LuaValue.valueOf(libsDir.toString()));

                LuaValue packageValue = g.get("package");
                if (packageValue.istable()) {
                    String packagePath = libsDir.resolve("?.lua").toString().replace("\\", "/")
                            + ";" + libsDir.resolve("?/init.lua").toString().replace("\\", "/");
                    packageValue.set("path", LuaValue.valueOf(packagePath));
                }
            }

            if (LuaRuntime.getLuaLoadingDir() != null) {
                g.set("LUA_LOADING_DIR", LuaValue.valueOf(LuaRuntime.getLuaLoadingDir().toString()));
            }

            if (LuaRuntime.getLuaGeneratedDir() != null) {
                g.set("LUA_GENERATED_DIR", LuaValue.valueOf(LuaRuntime.getLuaGeneratedDir().toString()));
            }

            if (LuaRuntime.getLuaGeneratedDataPacksDir() != null) {
                g.set("LUA_GENERATED_DATAPACKS_DIR", LuaValue.valueOf(LuaRuntime.getLuaGeneratedDataPacksDir().toString()));
            }

            g.set("SCRIPT_PATH", LuaValue.NIL);
            g.set("SCRIPT_DIR", LuaValue.NIL);
            g.set("require", createRequireFunction(g));
        } catch (Exception e) {
            LuaLogger.forgeError("Failed to initialize Lua sandbox globals", e);
        }

        return g;
    }

    private OneArgFunction createRequireFunction(Globals globals) {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue moduleArg) {
                String moduleName = moduleArg.checkjstring();
                Path modulePath = resolveModulePath(moduleName);

                if (modulePath == null) {
                    throw new LuaError("require: module not found: " + moduleName);
                }

                LuaValue cached = requiredModules.get(modulePath);
                if (cached != null) {
                    return cached;
                }

                try {
                    LuaValue chunk = globals.load(Files.readString(modulePath), modulePath.toString());
                    LuaValue result = chunk.call();
                    LuaValue stored = result.isnil() ? LuaValue.TRUE : result;
                    requiredModules.put(modulePath, stored);
                    return stored;
                } catch (Exception e) {
                    throw new LuaError("require failed for " + moduleName + ": " + e.getMessage());
                }
            }
        };
    }

    private Path resolveModulePath(String moduleName) {
        Path libsDir = LuaRuntime.getLuaLibsDir();
        if (libsDir == null) {
            return null;
        }

        String relative = moduleName.replace('.', '/');
        Path direct = libsDir.resolve(relative + ".lua").normalize();
        if (Files.exists(direct)) {
            return direct;
        }

        Path init = libsDir.resolve(relative).resolve("init.lua").normalize();
        if (Files.exists(init)) {
            return init;
        }

        return null;
    }

    public Globals execute(Path script) {
        try {
            if (!Files.exists(script)) {
                LuaLogger.forgeError("Lua script not found: {}", script);
                return null;
            }

            LuaLogger.forgeInfo("LuaVM executing {}", script.getFileName());
            LuaLogger.info("Executing " + script.getFileName());

            Globals scriptGlobals = createSandboxGlobals(worldDir);
            scriptGlobals.set("SCRIPT_PATH", LuaValue.valueOf(script.toString()));
            scriptGlobals.set("SCRIPT_DIR", LuaValue.valueOf(script.getParent().toString()));

            LuaValue chunk = scriptGlobals.load(Files.readString(script), script.toString());
            chunk.call();

            this.globals = scriptGlobals;
            return scriptGlobals;
        } catch (LuaError e) {
            LuaLogger.error("LuaError in " + script.getFileName(), e);
        } catch (Exception e) {
            LuaLogger.error("Exception executing Lua " + script.getFileName(), e);
        }

        return null;
    }

    public void callIfExists(String function) {
        if (globals == null) {
            return;
        }

        try {
            LuaValue func = globals.get(function);
            if (!func.isnil()) {
                func.call();
            }
        } catch (LuaError e) {
            LuaLogger.forgeError("LuaError calling function {}", function, e);
        }
    }

    public void set(String name, Object value) {
        if (globals == null) {
            return;
        }

        try {
            if (value instanceof Integer i) {
                globals.set(name, LuaValue.valueOf(i));
            } else if (value instanceof Double d) {
                globals.set(name, LuaValue.valueOf(d));
            } else if (value instanceof Boolean b) {
                globals.set(name, LuaValue.valueOf(b));
            } else {
                globals.set(name, LuaValue.valueOf(String.valueOf(value)));
            }
        } catch (Exception e) {
            LuaLogger.forgeError("Error setting Lua variable {}", name, e);
        }
    }

    public void remove(String name) {
        if (globals == null) {
            return;
        }
        globals.set(name, LuaValue.NIL);
    }

    public Globals getGlobals() {
        return globals;
    }
}
