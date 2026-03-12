package de.rsev.minecraft.forge.controller.lua.vm;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import de.rsev.minecraft.forge.controller.lua.bridge.commands.LuaCommandHandler;
import de.rsev.minecraft.forge.controller.lua.script.LuaScriptContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Zentraler Manager für alle Lua-VMs.
 *
 * Verwaltet getrennt:
 * - Event-VMs (Lua-Skripte für Events)
 * - Command-VMs (Lua-Skripte für Commands)
 * - Dynamische Lua-Commands
 * - Command-Beschreibungen
 */
public class LuaVMManager {

    /** Lua-VMs für Event-Skripte */
    private static final List<LuaScriptContainer> EVENT_VMS = new ArrayList<>();

    /** Lua-VMs für Command-Skripte */
    private static final List<LuaScriptContainer> COMMAND_VMS = new ArrayList<>();

    /** Dynamisch registrierte Lua-Commands */
    private static final Map<String, LuaCommandHandler> DYNAMIC_COMMANDS = new HashMap<>();

    /** Beschreibungen für dynamische Commands */
    private static final Map<String, String> COMMAND_DESCRIPTIONS = new HashMap<>();

    /**
     * Registriert eine Event-VM.
     * Verhindert doppelte Registrierung desselben Skripts.
     */
    public static void registerEvent(LuaScriptContainer container) {

        String name = container.getScriptPath().getFileName().toString();

        // Prüfen, ob Skript bereits geladen wurde
        boolean alreadyLoaded = EVENT_VMS.stream()
                .anyMatch(c -> c.getScriptPath().equals(container.getScriptPath()));

        if (alreadyLoaded) {
            LuaLogger.forgeWarn("registerEvent skipped → script already loaded: {}", name);
            return;
        }

        EVENT_VMS.add(container);
        LuaLogger.forgeInfo("Event VM registered → {}", name);
    }

    /**
     * Registriert eine Command-VM.
     * Null-Schutz + Schutz vor doppelter Registrierung.
     */
    public static void registerCommand(LuaScriptContainer container) {

        if (container == null) {
            LuaLogger.forgeWarn("registerCommand skipped → container null");
            return;
        }

        String scriptName = container.getScriptPath().getFileName().toString();

        // Prüfen, ob Skript bereits geladen wurde
        boolean alreadyLoaded = COMMAND_VMS.stream()
                .anyMatch(c -> c.getScriptPath().equals(container.getScriptPath()));

        if (alreadyLoaded) {
            LuaLogger.forgeWarn("registerCommand skipped → script already loaded: {}", scriptName);
            return;
        }

        COMMAND_VMS.add(container);
        LuaLogger.forgeInfo("Command VM registered → {}", scriptName);
    }

    /**
     * Führt den Tick für alle VMs aus.
     * Wird typischerweise einmal pro Server-Tick aufgerufen.
     */
    public static void tickAll() {
        EVENT_VMS.forEach(LuaScriptContainer::tick);
        COMMAND_VMS.forEach(LuaScriptContainer::tick);
    }

    /**
     * Fährt alle VMs herunter und leert die Listen.
     */
    public static void shutdownAll() {
        EVENT_VMS.forEach(LuaScriptContainer::shutdown);
        COMMAND_VMS.forEach(LuaScriptContainer::shutdown);

        EVENT_VMS.clear();
        COMMAND_VMS.clear();
    }

    /** Beendet nur Event-VMs */
    public static void shutdownEventVMs() {
        EVENT_VMS.forEach(LuaScriptContainer::shutdown);
        EVENT_VMS.clear();
    }

    /** Beendet nur Command-VMs */
    public static void shutdownCommandVMs() {
        COMMAND_VMS.forEach(LuaScriptContainer::shutdown);
        COMMAND_VMS.clear();
    }

    /**
     * @return Liste der geladenen Event-Skriptdateien
     */
    public static List<String> getLoadedEventScripts() {
        return EVENT_VMS.stream()
                .map(c -> c.getScriptPath().getFileName().toString())
                .distinct()
                .toList();
    }

    /**
     * @return Liste der geladenen Command-Skriptdateien
     */
    public static List<String> getLoadedCommandScripts() {
        return COMMAND_VMS.stream()
                .map(c -> c.getScriptPath().getFileName().toString())
                .distinct()
                .toList();
    }

    /** @return Anzahl aktiver Event-VMs */
    public static int getEventVMCount() {
        return EVENT_VMS.size();
    }

    /** @return Anzahl aktiver Command-VMs */
    public static int getCommandVMCount() {
        return COMMAND_VMS.size();
    }

    /**
     * @return Unveränderliche Kopie aller dynamischen Commands
     */
    public static Map<String, LuaCommandHandler> getDynamicCommands() {
        return Map.copyOf(DYNAMIC_COMMANDS);
    }

    /**
     * Initialisierungspunkt (derzeit nur Logging).
     * Praktisch für spätere Erweiterungen.
     */
    public static void initialize() {
        LuaLogger.forgeInfo("LuaVMManager initialize()");
    }

    /**
     * Registriert ein dynamisches Lua-Command.
     */
    public static void registerDynamicCommand(String name, LuaCommandHandler handler) {
        DYNAMIC_COMMANDS.put(name, handler);
        LuaLogger.forgeInfo("Dynamic Lua command registered → {}", name);
    }

    /**
     * Setzt/aktualisiert die Beschreibung eines Commands.
     */
    public static void setCommandDescription(String name, String desc) {
        COMMAND_DESCRIPTIONS.put(name, desc);
    }

    /**
     * @return Beschreibung eines Commands oder Fallback
     */
    public static String getCommandDescription(String name) {
        return COMMAND_DESCRIPTIONS.getOrDefault(name, "Lua command");
    }

    /**
     * Entfernt ein dynamisches Lua-Command.
     */
    public static void unregisterDynamicCommand(String name) {

        if (DYNAMIC_COMMANDS.remove(name) != null) {
            LuaLogger.forgeInfo("Dynamic Lua command removed → {}", name);
        } else {
            LuaLogger.forgeWarn("Dynamic Lua command not found → {}", name);
        }
    }

    /**
     * Liefert den Handler eines dynamischen Commands.
     */
    public static LuaCommandHandler getDynamicCommand(String name) {
        return DYNAMIC_COMMANDS.get(name);
    }

    /** Utility-Klasse → keine Instanzen */
    private LuaVMManager() {}
}