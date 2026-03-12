package de.rsev.minecraft.forge.controller.command.lua.list;

import de.rsev.minecraft.forge.controller.command.ICommand;
import de.rsev.minecraft.forge.controller.lua.vm.LuaVMManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class ListEventsCommand implements ICommand {

    @Override
    public int getPermissionLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "events";
    }

    @Override
    public String getDescription() {
        return "Listet alle in dieser Welt registrierten lua event Scripte auf.";
    }

    @Override
    public int execute(CommandSourceStack source) {
        var scripts = LuaVMManager.getLoadedEventScripts();

        if (scripts.isEmpty()) {
            source.sendSuccess(() -> Component.literal("No Lua event scripts loaded"), false);
            return 1;
        }

        source.sendSuccess(() -> Component.literal("Loaded Lua Event Scripts:"), false);

        scripts.forEach(name ->
                source.sendSuccess(() -> Component.literal(" - " + name), false));

        return 1;
    }

}
