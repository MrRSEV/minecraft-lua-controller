package de.rsev.minecraft.forge.controller.command.lua.list;

import de.rsev.minecraft.forge.controller.command.ICommand;
import de.rsev.minecraft.forge.controller.lua.vm.LuaVMManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class ListCommandsCommand implements ICommand {

    @Override
    public int getPermissionLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "commands";
    }

    @Override
    public String getDescription() {
        return "Listet alle von Lua registrierten Commands auf";
    }

    @Override
    public int execute(CommandSourceStack source) {
        var scripts = LuaVMManager.getLoadedCommandScripts();

        if (scripts.isEmpty()) {
            source.sendSuccess(() -> Component.literal("No Lua command scripts loaded"), false);
            return 1;
        }

        source.sendSuccess(() -> Component.literal("Loaded Lua Command Scripts:"), false);

        scripts.forEach(name ->
                source.sendSuccess(() -> Component.literal(" - " + name), false));

        return 1;
    }

}
