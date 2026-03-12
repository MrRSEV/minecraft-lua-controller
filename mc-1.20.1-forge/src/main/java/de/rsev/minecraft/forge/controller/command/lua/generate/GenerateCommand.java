package de.rsev.minecraft.forge.controller.command.lua.generate;

import de.rsev.minecraft.forge.controller.command.ICommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class GenerateCommand implements ICommand {

    @Override
    public int getPermissionLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "generate";
    }

    @Override
    public String getDescription() {
        return "Generate Lua-related resources";
    }

    @Override
    public int execute(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal(
                "§eUsage: /lua generate <documentation>"
        ), false);
        return 1;
    }
}
