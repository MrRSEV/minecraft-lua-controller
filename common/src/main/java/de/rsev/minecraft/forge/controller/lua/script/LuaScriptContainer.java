package de.rsev.minecraft.forge.controller.lua.script;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import de.rsev.minecraft.forge.controller.lua.vm.LuaVM;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.LuaError;

import java.nio.file.Path;

public class LuaScriptContainer {

    private final Path scriptPath;
    private final LuaVM vm;
    private Globals globals;

    public LuaScriptContainer(Path scriptPath, LuaVM vm) {
        this.scriptPath = scriptPath;
        this.vm = vm;
    }

    // =========================
    // Script ausführen
    // =========================
    public void execute() {
        LuaLogger.forgeInfo("Executing container {}", scriptPath.getFileName());

        try {
            globals = vm.execute(scriptPath);
        } catch (LuaError e) {
            LuaLogger.forgeError("LuaError in {}", scriptPath.getFileName(), e);
        } catch (Exception e) {
            LuaLogger.forgeError("Error executing {}", scriptPath.getFileName(), e);
        }
    }

    // =========================
    // Tick Hook
    // =========================
    public void tick() {
        if (globals == null) return;

        try {
            LuaValue tickFunc = globals.get("tick");

            if (!tickFunc.isnil()) {
                tickFunc.call();
            }

        } catch (LuaError e) {
            LuaLogger.forgeError("Lua tick error in {}", scriptPath.getFileName(), e);
        }
    }

    // =========================
    // Shutdown Hook
    // =========================
    public void shutdown() {
        if (globals == null) return;

        try {
            LuaValue shutdownFunc = globals.get("shutdown");

            if (!shutdownFunc.isnil()) {
                shutdownFunc.call();
            }

        } catch (LuaError e) {
            LuaLogger.forgeError("Lua shutdown error in {}", scriptPath.getFileName(), e);
        }
    }

    // =========================
    // Getter
    // =========================
    public LuaVM getVm() {
        return vm;
    }

    public Path getScriptPath() {
        return scriptPath;
    }

    public Globals getGlobals() {
        return globals;
    }
}
