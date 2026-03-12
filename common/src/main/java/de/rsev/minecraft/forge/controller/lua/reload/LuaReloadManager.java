package de.rsev.minecraft.forge.controller.lua.reload;

import de.rsev.minecraft.forge.controller.lua.LuaRuntime;
import de.rsev.minecraft.forge.controller.lua.bridge.events.LuaEventBus;
import de.rsev.minecraft.forge.controller.lua.bridge.timer.LuaScheduler;
import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import de.rsev.minecraft.forge.controller.lua.bridge.timer.LuaTimerBridgeInternal;

/**
 * Verantwortlich für das Neuladen (Reload) der Lua-Umgebung.
 *
 * Unterstützt:
 * - Reload nur der Events
 * - Vollständiger Reload (Events + Commands + VMs)
 */
public class LuaReloadManager {

    /**
     * Lädt ausschließlich Lua-Events neu.
     * Bestehende Scheduler/Events werden vorher bereinigt.
     */
    public static void reloadEvents() {

        // Aktuelles Weltverzeichnis abrufen
        var worldDir = LuaRuntime.getCurrentWorldDir();

        // Falls keine Welt aktiv ist → Reload überspringen
        if (worldDir == null) {
            LuaLogger.forgeWarn("Lua event reload skipped → worldDir is null");
            return;
        }

        LuaLogger.forgeInfo("Reloading Lua events...");

        // Bestehende Event-Registrierungen entfernen
        LuaEventBus.clear();

        // Scheduler zurücksetzen (Timer/Tasks stoppen)
        LuaScheduler.clear();

        // Events neu aus den Lua-Dateien laden
        LuaRuntime.reloadEvents(worldDir);

        LuaLogger.forgeInfo("Lua events reloaded");
    }

    /**
     * Führt einen vollständigen Lua-Reload durch.
     *
     * Ablauf:
     * 1️⃣ Alle Bridges / Scheduler / Timer bereinigen
     * 2️⃣ Alte Lua-VMs sauber herunterfahren
     * 3️⃣ Events & Commands neu laden
     */
    public static void reloadAll() {

        // Aktuelles Weltverzeichnis abrufen
        var worldDir = LuaRuntime.getCurrentWorldDir();

        // Falls keine Welt aktiv ist → Reload überspringen
        if (worldDir == null) {
            LuaLogger.forgeWarn("Lua reload skipped → worldDir is null");
            return;
        }

        LuaLogger.forgeInfo("Reloading Lua...");

        // ✅ 1️⃣ Laufzeitstrukturen bereinigen

        // Entfernt registrierte Lua-Events
        LuaEventBus.clear();

        // Stoppt geplante Tasks / Scheduler
        LuaScheduler.clear();

        // Stoppt & entfernt interne Timer
        LuaTimerBridgeInternal.clear();

        // ✅ 2️⃣ Alte Lua-VMs herunterfahren
        LuaRuntime.shutdown();

        // ✅ 3️⃣ Lua neu laden

        // Events neu laden
        LuaRuntime.reloadEvents(worldDir);

        // Commands neu registrieren
        LuaRuntime.reloadCommands();

        LuaLogger.forgeInfo("Lua fully reloaded");
    }

    /**
     * Privater Konstruktor verhindert Instanziierung.
     */
    private LuaReloadManager() {}
}