package de.rsev.minecraft.forge.controller.lua.bridge.config;

import de.rsev.minecraft.forge.controller.lua.tools.documentation.*;

public class ConfigDocBootstrap {

    private static boolean INITIALIZED = false;

    public static void init() {

        if (INITIALIZED) return;
        INITIALIZED = true;

        // =========================
        // Config.load
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.CONFIG, new LuaMethodDoc(
                        "Config.load",
                        "Config.load(path)",
                        "Lädt eine Config-Datei und gibt eine LuaTable zurück.",
                        """
                        local config = Config.load("config.lua")

                        if config then
                            System.print(config.someValue)
                        end
                        """,
                        "table | nil"
                )
        );

        // =========================
        // Config.save
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.CONFIG, new LuaMethodDoc(
                        "Config.save",
                        "Config.save(path, table)",
                        "Speichert eine LuaTable als Config-Datei.",
                        """
                        local cfg = {
                            enabled = true,
                            difficulty = "hard"
                        }

                        Config.save("config.lua", cfg)
                        """,
                        "boolean"
                )
        );

        // =========================
        // Typischer Workflow
        // =========================
       LuaDocumentationRegistry.register(LuaDocModule.CONFIG, new LuaMethodDoc(
                        "Config Workflow",
                        "load → modify → save",
                        "Typisches Muster zum Arbeiten mit Configs.",
                        """
                        local cfg = Config.load("settings.lua") or {}

                        cfg.counter = (cfg.counter or 0) + 1

                        Config.save("settings.lua", cfg)
                        """,
                        "—"
                )
        );
    }

    private ConfigDocBootstrap() {}
}