package de.rsev.minecraft.forge.controller.lifecycle;

import de.rsev.minecraft.forge.controller.RSEVControllerMod;
import de.rsev.minecraft.forge.controller.io.LuaDirectoryManager;
import de.rsev.minecraft.forge.controller.io.LuaPathResolver;
import de.rsev.minecraft.forge.controller.lua.LuaRuntime;
import de.rsev.minecraft.forge.controller.lua.ForgeLuaPlatformContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.nio.file.Path;

@Mod.EventBusSubscriber(modid = RSEVControllerMod.MODID)
public class WorldLifecycleHandler {

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {

        if (!(event.getLevel() instanceof ServerLevel level)) return;

        if (level.dimension() != Level.OVERWORLD) return;

        MinecraftServer server = level.getServer();
        String worldName = LuaPathResolver.resolveWorldName(server);
        Path worldDir = LuaPathResolver.getWorldDir(worldName);

        try {
            LuaDirectoryManager.prepareWorldDir(server);
        } catch (IOException e) {
            RSEVControllerMod.LOGGER.error("Failed to prepare Lua world directory on level load", e);
            return;
        }

        ForgeLuaPlatformContext.setCurrentWorldDir(worldDir);
        LuaRuntime.onWorldLoad();
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {

        if (!(event.getLevel() instanceof ServerLevel level)) return;

        if (level.dimension() != Level.OVERWORLD) return;

        LuaRuntime.onWorldUnload();
    }
}
