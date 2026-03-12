package de.rsev.minecraft.forge.controller.command.lua.list;

import de.rsev.minecraft.forge.controller.command.ICommand;
import de.rsev.minecraft.forge.controller.lua.vm.LuaVMManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class ListVMsCommand implements ICommand {

    @Override
    public int getPermissionLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "vms";
    }

    @Override
    public String getDescription() {
        return "Lists all active Lua VMs grouped by type";
    }

    @Override
    public int execute(CommandSourceStack source) {

        var eventScripts = LuaVMManager.getLoadedEventScripts();
        var commandScripts = LuaVMManager.getLoadedCommandScripts();

        if (eventScripts.isEmpty() && commandScripts.isEmpty()) {
            source.sendSuccess(() -> Component.literal("No Lua VMs loaded"), false);
            return 1;
        }

        source.sendSuccess(() -> Component.literal("Loaded Lua VMs:"), false);

        if (!eventScripts.isEmpty()) {
            source.sendSuccess(() -> Component.literal("[EVENT VMs]"), false);
            eventScripts.forEach(name ->
                    source.sendSuccess(() -> Component.literal(" - " + name), false));
        }

        if (!commandScripts.isEmpty()) {
            source.sendSuccess(() -> Component.literal("[COMMAND VMs]"), false);
            commandScripts.forEach(name ->
                    source.sendSuccess(() -> Component.literal(" - " + name), false));
        }

        return 1;
    }
}
