package de.rsev.minecraft.forge.controller.command.lua.list;

import de.rsev.minecraft.forge.controller.command.ICommand;
import de.rsev.minecraft.forge.controller.lua.bridge.timer.LuaScheduler;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class ListSchedulerCommand implements ICommand {

    @Override
    public int getPermissionLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "scheduler";
    }

    @Override
    public String getDescription() {
        return "Zeigt aktive Lua scheduler tasks";
    }

    @Override
    public int execute(CommandSourceStack source) {

        int count = LuaScheduler.size();

        source.sendSystemMessage(Component.literal("§6Lua Scheduler Tasks: §f" + count));

        return 1;
    }
}
