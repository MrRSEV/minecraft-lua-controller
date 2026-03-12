package de.rsev.minecraft.forge.controller.command.lua.list;

import de.rsev.minecraft.forge.controller.command.ICommand;
import de.rsev.minecraft.forge.controller.lua.bridge.timer.LuaTimerBridgeInternal;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class ListTimersCommand implements ICommand {

    @Override
    public int getPermissionLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "timers";
    }

    @Override
    public String getDescription() {
        return "Zeigt aktive Lua Timer";
    }

    @Override
    public int execute(CommandSourceStack source) {

        int count = LuaTimerBridgeInternal.size();

        source.sendSystemMessage(Component.literal("§6Lua Timers: §f" + count));

        return 1;
    }
}
