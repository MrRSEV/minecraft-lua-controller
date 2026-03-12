package de.rsev.minecraft.forge.controller.command.lua.list;

import de.rsev.minecraft.forge.controller.command.ICommand;
import de.rsev.minecraft.forge.controller.lua.LuaRuntime;
import de.rsev.minecraft.forge.controller.lua.script.discovery.LuaScriptDiscovery;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;

public class ListFilesCommand implements ICommand {

    @Override
    public int getPermissionLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "files";
    }

    @Override
    public String getDescription() {
        return "Listet alle in dieser Welt registrierten Lua Scripte auf.";
    }

    @Override
    public int execute(CommandSourceStack source) {
        Path worldDir = LuaRuntime.getCurrentWorldDir();

        if (worldDir == null) {
            source.sendFailure(Component.literal("WorldDir not available"));
            return 0;
        }

        try {
            var scripts = LuaScriptDiscovery.discover(worldDir);

            if (scripts.isEmpty()) {
                source.sendSuccess(() -> Component.literal("No Lua files found"), false);
                return 1;
            }

            source.sendSuccess(() -> Component.literal("Lua Files:"), false);

            scripts.forEach(path -> {
                String relative = worldDir.relativize(path).toString();
                source.sendSuccess(() -> Component.literal(" - " + relative), false);
            });

        } catch (Exception e) {
            source.sendFailure(Component.literal("Error listing Lua files"));
            e.printStackTrace();
        }

        return 1;
    }

}
