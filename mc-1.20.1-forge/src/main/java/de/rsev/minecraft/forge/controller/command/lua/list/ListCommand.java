package de.rsev.minecraft.forge.controller.command.lua.list;

import de.rsev.minecraft.forge.controller.command.ICommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class ListCommand implements ICommand {

    @Override
    public int getPermissionLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "List Lua debug information";
    }

    @Override
    public int execute(CommandSourceStack source) {
        source.sendSystemMessage(
                Component.literal("§eUsage: /lua list <commands|events|files|runtimetasks|scheuduler|timers|vms>")
        );
        return 1;
    }
}
