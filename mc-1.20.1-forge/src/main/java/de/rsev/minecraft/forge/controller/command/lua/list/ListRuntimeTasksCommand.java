package de.rsev.minecraft.forge.controller.command.lua.list;

import de.rsev.minecraft.forge.controller.command.ICommand;
import de.rsev.minecraft.forge.controller.lua.bridge.timer.LuaScheduler;
import de.rsev.minecraft.forge.controller.lua.bridge.timer.LuaTimerBridgeInternal;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class ListRuntimeTasksCommand implements ICommand {

    @Override
    public int getPermissionLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "runtimetasks";
    }

    @Override
    public String getDescription() {
        return "Shows all Lua runtime tasks";
    }

    @Override
    public int execute(CommandSourceStack source) {

        int timers = LuaTimerBridgeInternal.size();
        int scheduler = LuaScheduler.size();

        source.sendSystemMessage(Component.literal("§6Lua Debug:"));
        source.sendSystemMessage(Component.literal("§eTimers: §f" + timers));
        source.sendSystemMessage(Component.literal("§eScheduler Tasks: §f" + scheduler));

        return 1;
    }
}
