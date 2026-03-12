package de.rsev.minecraft.forge.controller.lua.bridge.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public class LuaCommandWrapper {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("lua")
                        .executes(ctx -> {
                            ctx.getSource().sendSuccess(() -> 
                                    net.minecraft.network.chat.Component.literal("Lua command executed"), false);
                            return 1;
                        })
        );
    }

    private LuaCommandWrapper() {}
}
