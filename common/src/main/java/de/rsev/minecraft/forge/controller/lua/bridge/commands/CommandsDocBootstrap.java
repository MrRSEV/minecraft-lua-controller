package de.rsev.minecraft.forge.controller.lua.bridge.commands;

import de.rsev.minecraft.forge.controller.lua.tools.documentation.*;

public class CommandsDocBootstrap {

    private static boolean INITIALIZED = false;

    public static void init() {

        if (INITIALIZED) return;
        INITIALIZED = true;

        // =========================
        // Commands.register(name, func)
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.COMMANDS, new LuaMethodDoc(
                        "Commands.register",
                        "Commands.register(name, function)",
                        "Registriert einen Lua Command.",
                        """
                        Commands.register("hello", function(ctx)
                            System.print("Hallo " .. ctx.player:getName())
                        end)
                        """,
                        "nil"
                )
        );

        // =========================
        // Commands.register(name, description, func)
        // =========================
       LuaDocumentationRegistry.register(LuaDocModule.COMMANDS, new LuaMethodDoc(
                        "Commands.register (mit Beschreibung)",
                        "Commands.register(name, description, function)",
                        "Registriert einen Lua Command mit Beschreibung.",
                        """
                        Commands.register("heal", "Heilt den Spieler", function(ctx)
                            System.print("Healing player...")
                        end)
                        """,
                        "nil"
                )
        );

        // =========================
        // Commands.unregister
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.COMMANDS, new LuaMethodDoc(
                        "Commands.unregister",
                        "Commands.unregister(name)",
                        "Entfernt einen Lua Command.",
                        """
                        Commands.unregister("hello")
                        """,
                        "nil"
                )
        );

        // =========================
        // CommandContext.player
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.COMMANDS, new LuaMethodDoc(
                        "ctx.player",
                        "ctx.player",
                        "PlayerWrapper des ausführenden Spielers.",
                        """
                        Commands.register("whoami", function(ctx)
                            System.print(ctx.player:getName())
                        end)
                        """,
                        "LuaPlayer"
                )
        );

        // =========================
        // CommandContext.args
        // =========================
       LuaDocumentationRegistry.register(LuaDocModule.COMMANDS, new LuaMethodDoc(
                        "ctx.args",
                        "ctx.args[index]",
                        "Argumente des Commands (1-basiert).",
                        """
                        Commands.register("echo", function(ctx)
                            System.print(ctx.args[1])
                        end)
                        """,
                        "string"
                )
        );
    }

    private CommandsDocBootstrap() {}
}