package de.rsev.minecraft.forge.controller.paper.lua;

import de.rsev.minecraft.forge.controller.lua.LuaPlatformContext;
import de.rsev.minecraft.forge.controller.paper.io.PaperLuaPathResolver;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public final class PaperLuaPlatformContext implements LuaPlatformContext {

    private static JavaPlugin plugin;
    private static World currentWorld;

    public static void bind(JavaPlugin javaPlugin, World world) {
        plugin = javaPlugin;
        currentWorld = world;
    }

    @Override
    public Path getCurrentWorldDir() {
        return plugin != null && currentWorld != null
                ? PaperLuaPathResolver.getWorldDir(plugin, currentWorld)
                : null;
    }

    @Override
    public Path getLuaRootDir() {
        return plugin != null ? PaperLuaPathResolver.getLuaRoot(plugin) : null;
    }

    @Override
    public Path getLuaGlobalDir() {
        return getLuaRootDir();
    }

    @Override
    public Path getLuaWorldDir() {
        return getCurrentWorldDir();
    }

    @Override
    public Path getLuaLogDir() {
        return plugin != null ? PaperLuaPathResolver.getLogDir(plugin) : null;
    }

    @Override
    public Path getLuaLibsDir() {
        return plugin != null ? PaperLuaPathResolver.getLibsDir(plugin) : null;
    }
}
