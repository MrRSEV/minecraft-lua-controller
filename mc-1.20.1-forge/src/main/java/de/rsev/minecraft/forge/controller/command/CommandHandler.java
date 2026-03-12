package de.rsev.minecraft.forge.controller.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import de.rsev.minecraft.forge.controller.lua.vm.LuaVMManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {

    private static final CommandNode ROOT = new CommandNode("lua", 3);


    public static void registerLuaCommand(String namespace, ICommand command) {
        ROOT.addPath(namespace, command);
    }

    public static void registerCommand(String namespace, ICommand command) {
        ROOT.addPath(namespace, command);
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(buildNode(ROOT));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildNode(CommandNode node) {
        LiteralArgumentBuilder<CommandSourceStack> builder =
                Commands.literal(node.name)
                        .requires(src -> src.hasPermission(node.permissionLevel));

        if (node.command != null) {
            builder.executes(ctx -> node.command.execute(ctx.getSource()));
        }

        node.children.values().forEach(child ->
                builder.then(buildNode(child))
        );

        return builder;
    }

    // package-private reicht völlig
    static class CommandNode {
        final String name;
        final int permissionLevel;
        ICommand command;
        final Map<String, CommandNode> children = new HashMap<>();

        CommandNode(String name, int permissionLevel) {
            this.name = name;
            this.permissionLevel = permissionLevel;
        }

        void setCommand(ICommand command) {
            this.command = command;
        }

        void addChild(CommandNode child) {
            children.put(child.name, child);
        }

        void addPath(String path, ICommand command) {
            String[] parts = path.split("\\.");
            CommandNode current = this;

            for (String part : parts) {
                current = current.children.computeIfAbsent(
                        part,
                        p -> new CommandNode(p, 3)
                );
            }

            current.setCommand(command);
        }
    }

    public static Map<String, ICommand> getAllCommands() {

        Map<String, ICommand> map = new HashMap<>();

        // Statische Commands
        collectCommands(ROOT, "", map);

        // Dynamische Lua Commands
        LuaVMManager.getDynamicCommands().forEach((name, handler) -> {
            String desc = LuaVMManager.getCommandDescription(name);
            map.put(ROOT.name + "." + name, new ICommand() {
                @Override public int getPermissionLevel() { return 0; }
                @Override public String getName() { return name; }
                @Override public String getDescription() { return desc; }
                @Override public int execute(CommandSourceStack source) { return 1; }
            });
        });

        return map;
    }


    private static void collectCommands(CommandNode node, String path, Map<String, ICommand> map) {
        String currentPath = path.isEmpty() ? node.name : path + "." + node.name;

        if (node.command != null) {
            map.put(currentPath, node.command);
        }

        node.children.values().forEach(child ->
                collectCommands(child, currentPath, map));
    }


    private CommandHandler() {}
}
