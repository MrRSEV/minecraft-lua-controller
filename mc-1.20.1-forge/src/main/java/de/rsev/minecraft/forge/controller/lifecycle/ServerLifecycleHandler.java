package de.rsev.minecraft.forge.controller.lifecycle;

import de.rsev.minecraft.forge.controller.RSEVControllerMod;
import de.rsev.minecraft.forge.controller.command.CommandHandler;
import de.rsev.minecraft.forge.controller.command.HelpCommand;
import de.rsev.minecraft.forge.controller.command.lua.ReloadCommand;
import de.rsev.minecraft.forge.controller.command.lua.generate.GenerateCommand;
import de.rsev.minecraft.forge.controller.command.lua.generate.GenerateDocumentationCommand;
import de.rsev.minecraft.forge.controller.command.lua.list.ListCommand;
import de.rsev.minecraft.forge.controller.command.lua.list.ListCommandsCommand;
import de.rsev.minecraft.forge.controller.command.lua.list.ListEventsCommand;
import de.rsev.minecraft.forge.controller.command.lua.list.ListFilesCommand;
import de.rsev.minecraft.forge.controller.command.lua.list.ListRuntimeTasksCommand;
import de.rsev.minecraft.forge.controller.command.lua.list.ListSchedulerCommand;
import de.rsev.minecraft.forge.controller.command.lua.list.ListTimersCommand;
import de.rsev.minecraft.forge.controller.command.lua.list.ListVMsCommand;
import de.rsev.minecraft.forge.controller.io.LuaDirectoryManager;
import de.rsev.minecraft.forge.controller.io.LuaPathResolver;
import de.rsev.minecraft.forge.controller.lua.ForgeLuaPlatformContext;
import de.rsev.minecraft.forge.controller.lua.ForgeLuaServerAdapter;
import de.rsev.minecraft.forge.controller.lua.LuaRuntime;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = RSEVControllerMod.MODID)
public class ServerLifecycleHandler {

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        MinecraftServer server = event.getServer();

        try {
            String worldName = LuaPathResolver.resolveWorldName(server);
            LuaDirectoryManager.prepareRuntimeDirectories(server);

            Path worldDir = LuaPathResolver.getWorldDir(worldName);
            ForgeLuaPlatformContext.bind(worldDir);
            LuaRuntime.setServerAdapter(new ForgeLuaServerAdapter(server));

            RSEVControllerMod.LOGGER.info("Server about to start -> Lua ready (world: {})", worldName);
        } catch (IOException e) {
            RSEVControllerMod.LOGGER.error("Lua directory preparation failed", e);
        }
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        LuaRuntime.shutdown();
        RSEVControllerMod.LOGGER.info("Server stopping -> Lua shutdown");
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandHandler.registerLuaCommand("help", new HelpCommand());

        CommandHandler.registerLuaCommand("reload", new ReloadCommand());

        CommandHandler.registerLuaCommand("generate", new GenerateCommand());
        CommandHandler.registerLuaCommand(
                "generate.documentation",
                new GenerateDocumentationCommand()
        );

        CommandHandler.registerLuaCommand("list", new ListCommand());
        CommandHandler.registerLuaCommand("list.timers", new ListTimersCommand());
        CommandHandler.registerLuaCommand("list.scheduler", new ListSchedulerCommand());
        CommandHandler.registerLuaCommand(
                "list.runtimetasks",
                new ListRuntimeTasksCommand()
        );
        CommandHandler.registerLuaCommand("list.events", new ListEventsCommand());
        CommandHandler.registerLuaCommand("list.commands", new ListCommandsCommand());
        CommandHandler.registerLuaCommand("list.files", new ListFilesCommand());
        CommandHandler.registerLuaCommand("list.vms", new ListVMsCommand());

        CommandHandler.register(event.getDispatcher());
    }
}
