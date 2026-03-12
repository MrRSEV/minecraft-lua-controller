package de.rsev.minecraft.forge.controller.lua;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import de.rsev.minecraft.forge.controller.lua.bridge.commands.LuaCommandHandler;
import de.rsev.minecraft.forge.controller.lua.bridge.timer.LuaScheduler;
import de.rsev.minecraft.forge.controller.lua.bridge.timer.LuaTimerBridgeInternal;
import de.rsev.minecraft.forge.controller.lua.script.LuaScriptContainer;
import de.rsev.minecraft.forge.controller.lua.script.LuaScriptLoader;
import de.rsev.minecraft.forge.controller.lua.script.discovery.LuaScriptDiscovery;
import de.rsev.minecraft.forge.controller.lua.vm.LuaVMManager;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Minecraft-freie Laufzeitkoordination fuer Lua.
 * Host-spezifische Verzeichnisse und Weltbindung kommen ueber {@link LuaPlatformContext}.
 */
public class LuaRuntime {

    private static boolean INITIAL_LOAD_DONE = false;
    private static LuaServerAdapter SERVER;
    private static LuaPlatformContext PLATFORM_CONTEXT;
    private static boolean DEV_MODE = true;

    public static boolean isDevMode() {
        return DEV_MODE;
    }

    public static void setServerAdapter(LuaServerAdapter adapter) {
        SERVER = adapter;
    }

    public static LuaServerAdapter getServer() {
        return SERVER;
    }

    public static void setPlatformContext(LuaPlatformContext context) {
        PLATFORM_CONTEXT = context;
    }

    public static LuaPlatformContext getPlatformContext() {
        return PLATFORM_CONTEXT;
    }

    public static Path getCurrentWorldDir() {
        return PLATFORM_CONTEXT != null ? PLATFORM_CONTEXT.getCurrentWorldDir() : null;
    }

    public static Path getLuaRootDir() {
        return PLATFORM_CONTEXT != null ? PLATFORM_CONTEXT.getLuaRootDir() : null;
    }

    public static Path getLuaGlobalDir() {
        return PLATFORM_CONTEXT != null ? PLATFORM_CONTEXT.getLuaGlobalDir() : null;
    }

    public static Path getLuaWorldDir() {
        return PLATFORM_CONTEXT != null ? PLATFORM_CONTEXT.getLuaWorldDir() : null;
    }

    public static Path getLuaLogDir() {
        return PLATFORM_CONTEXT != null ? PLATFORM_CONTEXT.getLuaLogDir() : null;
    }

    public static Path getLuaLibsDir() {
        return PLATFORM_CONTEXT != null ? PLATFORM_CONTEXT.getLuaLibsDir() : null;
    }

    public static Path getLuaLoadingDir() {
        return PLATFORM_CONTEXT != null ? PLATFORM_CONTEXT.getLuaLoadingDir() : null;
    }

    public static Path getLuaGeneratedDir() {
        Path globalDir = getLuaGlobalDir();
        return globalDir != null ? globalDir.resolve("generated") : null;
    }

    public static Path getLuaGeneratedDataPacksDir() {
        Path generatedDir = getLuaGeneratedDir();
        return generatedDir != null ? generatedDir.resolve("datapacks") : null;
    }

    public static void onWorldLoad() {
        Path currentWorldDir = getCurrentWorldDir();

        if (currentWorldDir == null) {
            LuaLogger.warn("LuaRuntime.onWorldLoad skipped -> CURRENT_WORLD_DIR null");
            return;
        }

        if (INITIAL_LOAD_DONE) {
            return;
        }
        INITIAL_LOAD_DONE = true;

        try {
            Path eventsDir = currentWorldDir.resolve("events");
            Path commandsDir = currentWorldDir.resolve("commands");

            LuaLogger.info("Initial Lua load -> events: {}", eventsDir);
            LuaScriptDiscovery.discover(eventsDir).forEach(LuaRuntime::loadEventScript);

            LuaLogger.info("Initial Lua load -> commands: {}", commandsDir);
            LuaScriptDiscovery.discover(commandsDir).forEach(LuaRuntime::loadCommandScript);

            LuaLogger.info("Lua initial load completed");
        } catch (Exception e) {
            LuaLogger.error("Lua initial load failed", e);
        }
    }

    public static void onWorldUnload() {
        LuaLogger.info("LuaRuntime.onWorldUnload -> shutting down Lua");
        shutdown();
        INITIAL_LOAD_DONE = false;
    }

    public static void tick() {
        LuaTimerBridgeInternal.tick();
        LuaScheduler.tick();
        LuaVMManager.tickAll();
    }

    public static void shutdown() {
        LuaVMManager.shutdownAll();
        LuaLogger.shutdown();
    }

    public static LuaScriptContainer loadEventScript(Path script) {
        LuaLogger.info("Loading EVENT Lua script {}", script.getFileName());

        LuaScriptContainer container = LuaScriptLoader.load(script);
        LuaVMManager.registerEvent(container);

        return container;
    }

    public static LuaScriptContainer loadCommandScript(Path script) {
        LuaLogger.info("Loading COMMAND Lua script {}", script.getFileName());

        LuaScriptContainer container = LuaScriptLoader.load(script);
        LuaVMManager.registerCommand(container);

        return container;
    }

    public static void initialize() {
        LuaLogger.info("LuaRuntime initialize()");

        try {
            Path logDir = getLuaLogDir();

            if (logDir != null) {
                LuaLogger.init(logDir);
                LuaLogger.info("LuaLogger initialize at LogDir {}", logDir);
            } else {
                LuaLogger.warn("LuaRuntime initialize() without platform log directory");
            }

            LuaVMManager.initialize();
        } catch (Exception e) {
            LuaLogger.error("LuaRuntime initialization failed", e);
        }
    }

    public record CommandContext(Object player, String[] args) {}

    public static void registerLuaCommand(String name, LuaCommandHandler handler) {
        if (name == null || name.isBlank()) {
            LuaLogger.warn("registerLuaCommand skipped -> name empty");
            return;
        }

        LuaLogger.info("Registering Lua command: {}", name);
        LuaVMManager.registerDynamicCommand(name, handler);
    }

    public static void unregisterLuaCommand(String name) {
        if (name == null || name.isBlank()) {
            LuaLogger.warn("unregisterLuaCommand skipped -> name empty");
            return;
        }

        LuaLogger.info("Unregistering Lua command: {}", name);

        try {
            LuaVMManager.unregisterDynamicCommand(name);
        } catch (Exception e) {
            LuaLogger.error("Failed to unregister Lua command: " + name, e);
        }
    }

    public static void reloadEvents(Path worldDir) {
        if (worldDir == null) {
            LuaLogger.warn("reloadEvents skipped -> worldDir null");
            return;
        }

        LuaVMManager.shutdownEventVMs();

        try {
            Path eventsDir = worldDir.resolve("events");

            if (!Files.exists(eventsDir)) {
                LuaLogger.warn("Lua events directory missing: {}", eventsDir);
                return;
            }

            LuaLogger.info("Reloading Lua events from {}", eventsDir);
            LuaScriptDiscovery.discover(eventsDir).forEach(LuaRuntime::loadEventScript);
            LuaLogger.info("Lua events reloaded");
        } catch (Exception e) {
            LuaLogger.error("Lua events reload failed", e);
        }
    }

    public static void reloadCommands() {
        Path currentWorldDir = getCurrentWorldDir();

        if (currentWorldDir == null) {
            LuaLogger.warn("reloadCommands skipped -> CURRENT_WORLD_DIR null");
            return;
        }

        LuaVMManager.shutdownCommandVMs();

        try {
            Path commandsDir = currentWorldDir.resolve("commands");

            if (!Files.exists(commandsDir)) {
                LuaLogger.warn("Lua commands directory missing: {}", commandsDir);
                return;
            }

            LuaLogger.info("Reloading Lua commands from {}", commandsDir);
            LuaScriptDiscovery.discover(commandsDir).forEach(LuaRuntime::loadCommandScript);
            LuaLogger.info("Lua commands reloaded");
        } catch (Exception e) {
            LuaLogger.error("Lua command reload failed", e);
        }
    }

    private LuaRuntime() {}
}
