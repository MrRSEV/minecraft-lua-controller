package de.rsev.minecraft.forge.controller.command;

import net.minecraft.commands.CommandSourceStack;

public interface ICommand {

    int getPermissionLevel();

    String getName();

    String getDescription();

    int execute(CommandSourceStack source);

    default boolean hasPermission(CommandSourceStack source) {
        return source.hasPermission(getPermissionLevel());
    }
}
