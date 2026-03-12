package de.rsev.minecraft.forge.controller.lua.bridge.events;

import de.rsev.minecraft.forge.controller.lua.tools.documentation.*;

public class EventsDocBootstrap {

    private static boolean INITIALIZED = false;

    public static void init() {

        if (INITIALIZED) return;
        INITIALIZED = true;

        // =========================
        // Events.on
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.EVENTS, new LuaMethodDoc(
                        "Events.on",
                        "Events.on(eventName, function)",
                        "Registriert einen Event Handler.",
                        """
                        Events.on("playerJoin", function(ctx)
                            System.print("Player joined: " .. ctx.player:getName())
                        end)
                        """,
                        "nil"
                )
        );

        // =========================
        // Event Context
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.EVENTS, new LuaMethodDoc(
                        "Event Context",
                        "function(ctx)",
                        "Events erhalten ein Context-Objekt.",
                        """
                        Events.on("blockBreak", function(ctx)
                            System.print(ctx.x .. ", " .. ctx.y .. ", " .. ctx.z)
                        end)
                        """,
                        "table"
                )
        );

        // =========================
        // ctx.player
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.EVENTS, new LuaMethodDoc(
                        "ctx.player",
                        "ctx.player",
                        "PlayerWrapper falls Event von Spieler ausgelöst wurde.",
                        """
                        Events.on("playerChat", function(ctx)
                            System.print(ctx.player:getName())
                        end)
                        """,
                        "LuaPlayer | nil"
                )
        );

        // =========================
        // ctx.args / custom data
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.EVENTS, new LuaMethodDoc(
                        "ctx.<data>",
                        "ctx.fieldName",
                        "Event-spezifische Daten.",
                        """
                        Events.on("blockBreak", function(ctx)
                            System.print(ctx.block)
                        end)
                        """,
                        "any"
                )
        );

        // =========================
        // Typisches Muster
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.EVENTS, new LuaMethodDoc(
                        "Event Workflow",
                        "register → react",
                        "Typische Event-Struktur.",
                        """
                        Events.on("onTick", function(ctx)
                            -- wird jeden Tick aufgerufen
                        end)
                        """,
                        "—"
                )
        );
    }

    private EventsDocBootstrap() {}
}