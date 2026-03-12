package de.rsev.minecraft.forge.controller.command;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class HelpCommand implements ICommand {

    @Override
    public int getPermissionLevel() {
        return 0;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Listet alle /lua commands auf.";
    }

    @Override
    public int execute(CommandSourceStack source) {

        var commands = CommandHandler.getAllCommands();

        source.sendSuccess(() -> Component.literal("Lua Commands:"), false);

        commands.forEach((name, cmd) -> {
            if (source.hasPermission(cmd.getPermissionLevel())) {
                source.sendSuccess(() -> Component.literal(
                        " /" + name.replace(".", " ")
                        + " → " + cmd.getDescription()
                ), false);
            }
        });

        return 1;
    }
}
