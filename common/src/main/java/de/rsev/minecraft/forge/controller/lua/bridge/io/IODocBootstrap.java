package de.rsev.minecraft.forge.controller.lua.bridge.io;

import de.rsev.minecraft.forge.controller.lua.tools.documentation.*;

public class IODocBootstrap {

    private static boolean INITIALIZED = false;

    public static void init() {

        if (INITIALIZED) return;
        INITIALIZED = true;

        // =========================
        // File.exists
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.IO, new LuaMethodDoc(
                "File.exists",
                "File.exists(path)",
                "Prüft, ob eine Datei existiert.",
                """
                if File.exists("data/config.lua") then
                    System.print("Datei existiert")
                end
                """,
                "boolean"
        ));

        // =========================
        // File.write
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.IO, new LuaMethodDoc(
                "File.write",
                "File.write(path, content)",
                "Schreibt Text in eine Datei.",
                """
                File.write("data/test.txt", "Hallo Lua")
                """,
                "boolean"
        ));

        // =========================
        // File.read
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.IO, new LuaMethodDoc(
                "File.read",
                "File.read(path)",
                "Liest den Inhalt einer Datei.",
                """
                local content = File.read("data/test.txt")

                if content then
                    System.print(content)
                end
                """,
                "string | nil"
        ));

        // =========================
        // Toml.parse
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.IO, new LuaMethodDoc(
                "Toml.parse",
                "Toml.parse(path)",
                "Lädt eine TOML Datei als LuaTable.",
                """
                local cfg = Toml.parse("data/config.toml")

                if cfg then
                    System.print(cfg.database.host)
                end
                """,
                "table | nil"
        ));

        // =========================
        // Toml.write
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.IO, new LuaMethodDoc(
                "Toml.write",
                "Toml.write(path, table)",
                "Speichert eine LuaTable als TOML Datei.",
                """
                local cfg = {
                    database = {
                        host = "localhost",
                        port = 3306
                    }
                }

                Toml.write("data/config.toml", cfg)
                """,
                "boolean"
        ));

        // =========================
        // Json.parse
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.IO, new LuaMethodDoc(
                "Json.parse",
                "Json.parse(path)",
                "Lädt eine JSON Datei als LuaTable.",
                """
                local data = Json.parse("data/config.json")

                if data then
                    System.print(data.name)
                end
                """,
                "table | nil"
        ));

        // =========================
        // Json.write
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.IO, new LuaMethodDoc(
                "Json.write",
                "Json.write(path, table)",
                "Speichert eine LuaTable als JSON Datei.",
                """
                Json.write("data/config.json", {
                    name = "Steve",
                    hp = 20
                })
                """,
                "boolean"
        ));

        // =========================
        // Json.encode
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.IO, new LuaMethodDoc(
                "Json.encode",
                "Json.encode(value)",
                "Konvertiert eine LuaTable oder einen Wert in JSON.",
                """
                local json = Json.encode({
                    name = "Steve",
                    hp = 20
                })

                System.print(json)
                """,
                "string | nil"
        ));

        // =========================
        // Json.decode
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.IO, new LuaMethodDoc(
                "Json.decode",
                "Json.decode(jsonString)",
                "Konvertiert einen JSON String in eine LuaTable.",
                """
                local tbl = Json.decode('{"name":"Steve","hp":20}')

                if tbl then
                    System.print(tbl.name)
                end
                """,
                "table | nil"
        ));

        // =========================
        // Yaml.parse
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.IO, new LuaMethodDoc(
                "Yaml.parse",
                "Yaml.parse(path)",
                "Lädt eine YAML Datei als LuaTable.",
                """
                local cfg = Yaml.parse("data/config.yml")

                if cfg then
                    System.print(cfg.version)
                end
                """,
                "table | nil"
        ));

        // =========================
        // Yaml.write
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.IO, new LuaMethodDoc(
                "Yaml.write",
                "Yaml.write(path, table)",
                "Speichert eine LuaTable als YAML Datei.",
                """
                Yaml.write("data/config.yml", {
                    name = "Steve",
                    hp = 20
                })
                """,
                "boolean"
        ));

        // =========================
        // Workflow Beispiel
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.IO, new LuaMethodDoc(
                "IO Workflow",
                "read → modify → write",
                "Typisches Muster für Dateiverarbeitung.",
                """
                local content = File.read("data/log.txt") or ""

                content = content .. "\\nNeue Zeile"

                File.write("data/log.txt", content)
                """,
                "—"
        ));

        // =========================
        // Sandbox Hinweis
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.IO, new LuaMethodDoc(
                "Sandbox Restriction",
                "Lua IO Sandbox",
                "Dateizugriffe sind auf den Lua Root beschränkt.",
                """
                -- Erlaubt:
                File.write("data/test.txt", "ok")

                -- Blockiert:
                File.write("../server.properties")
                """,
                "—"
        ));
    }

    private IODocBootstrap() {}
}