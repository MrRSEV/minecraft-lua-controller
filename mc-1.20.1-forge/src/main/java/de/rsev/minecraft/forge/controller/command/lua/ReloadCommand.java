package de.rsev.minecraft.forge.controller.command.lua;

import de.rsev.minecraft.forge.controller.command.ICommand;
import de.rsev.minecraft.forge.controller.lua.reload.LuaReloadManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class ReloadCommand implements ICommand {

    @Override
    public int getPermissionLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Läd alle registrierten world Lua Scripte einmal neu.";
    }

    @Override
    public int execute(CommandSourceStack source) {
        LuaReloadManager.reloadAll();
        source.sendSuccess(() -> Component.literal("§aLua reloaded"), false);
        return 1;
    }

}
