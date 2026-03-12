package de.rsev.minecraft.forge.controller.lua.bridge.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import de.rsev.minecraft.forge.controller.lua.bridge.player.ForgeLuaPlayerHandle;
import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerWrapper;
import de.rsev.minecraft.forge.controller.lua.vm.LuaVMManager;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Registriert und verarbeitet dynamische Lua-Commands.
 *
 * Ermöglicht:
 * /lua <command> [args...]
 *
 * Der eigentliche Command wird zur Laufzeit aus der Lua-VM aufgelöst.
 */
public class DynamicLuaCommandExecutor {

    /**
     * Registriert den Brigadier-Command-Baum.
     *
     * Struktur:
     * /lua
     *   └── <command>
     *        └── [args...]
     */
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
            Commands.literal("lua")

                // Erstes Argument: Command-Name (ein Wort)
                .then(Commands.argument("command", StringArgumentType.word())

                    // Ausführung ohne zusätzliche Argumente
                    .executes(ctx -> execute(
                            ctx.getSource(),
                            StringArgumentType.getString(ctx, "command"),
                            new String[0] // Leeres Argument-Array
                    ))

                    // Optional: Restliche Argumente (greedyString = gesamter Rest)
                    .then(Commands.argument("args", StringArgumentType.greedyString())
                        .executes(ctx -> {

                            // Command-Name abrufen
                            String command = StringArgumentType.getString(ctx, "command");

                            // Rohargumente abrufen
                            String rawArgs = StringArgumentType.getString(ctx, "args");

                            // Argumente sicher aufteilen
                            String[] args = rawArgs == null || rawArgs.isBlank()
                                    ? new String[0]
                                    : rawArgs.split(" ");

                            return execute(ctx.getSource(), command, args);
                        })
                    )
                )
        );
    }

    /**
     * Führt ein dynamisches Lua-Command aus.
     *
     * @param source      Command-Quelle (Spieler / Konsole / CommandBlock)
     * @param commandName Name des Lua-Commands
     * @param args        Übergebene Argumente
     * @return 1 bei Erfolg, 0 bei Fehler
     */
    private static int execute(CommandSourceStack source, String commandName, String[] args) {

        // Dynamischen Handler aus der Lua-VM abrufen
        LuaCommandHandler handler = LuaVMManager.getDynamicCommand(commandName);

        // Falls kein Handler existiert → Fehler melden
        if (handler == null) {
            source.sendFailure(Component.literal(
                    "Unknown Lua command: " + commandName
            ));
            return 0;
        }

        // Spieler abrufen (ohne Exception)
        // Wichtig: Kein getPlayerOrException(), damit Konsole sauber abgefangen wird
        ServerPlayer player = source.getPlayer();

        // Falls Command nicht von einem Spieler ausgeführt wurde
        if (player == null) {
            source.sendFailure(Component.literal(
                    "This Lua command can only be executed by a player."
            ));
            return 0;
        }

        
        Object playerWrapper = LuaPlayerBridge.wrap(new LuaPlayerWrapper(new ForgeLuaPlayerHandle(player)));
        
        try {
            // Lua-Command ausführen
            handler.execute(playerWrapper, args);
            return 1;

        } catch (Exception e) {

            // Fehlermeldung an Spieler senden
            source.sendFailure(Component.literal(
                    "Lua command failed: " + commandName
            ));

            // Debug-Ausgabe (besonders hilfreich im DevMode)
            e.printStackTrace();

            return 0;
        }
    }
}
