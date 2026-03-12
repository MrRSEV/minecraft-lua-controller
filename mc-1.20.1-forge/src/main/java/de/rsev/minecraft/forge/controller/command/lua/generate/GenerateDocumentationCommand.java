package de.rsev.minecraft.forge.controller.command.lua.generate;

import de.rsev.minecraft.forge.controller.command.ICommand;
import de.rsev.minecraft.forge.controller.lua.tools.documentation.LuaDocumentationBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class GenerateDocumentationCommand implements ICommand {

    @Override
    public int getPermissionLevel() {
        return 3;
    }

    @Override
    public String getName() {
        return "documentation";
    }

    @Override
    public int execute(CommandSourceStack source) {
        LuaDocumentationBuilder.generate();
        source.sendSuccess(() -> Component.literal("Lua documentation generated"), false);
        return 1;
    }

    @Override
    public String getDescription() {
        return "Generiert eine Lua Documentation um Lua Scripte mit ChatGPT generieren zu können.";
    }
}
