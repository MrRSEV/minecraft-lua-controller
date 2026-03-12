package de.rsev.minecraft.forge.controller.paper.io;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public final class PaperLuaPathResolver {

    public static Path getPluginDir(JavaPlugin plugin) {
        return plugin.getDataFolder().toPath();
    }

    public static Path getLuaRoot(JavaPlugin plugin) {
        return getPluginDir(plugin).resolve("lua");
    }

    public static Path getLogDir(JavaPlugin plugin) {
        return getLuaRoot(plugin).resolve("log");
    }

    public static Path getLibsDir(JavaPlugin plugin) {
        return getLuaRoot(plugin).resolve("libs");
    }

    public static Path getWorldDir(JavaPlugin plugin, World world) {
        return getLuaRoot(plugin).resolve("worlds").resolve(world.getName());
    }

    private PaperLuaPathResolver() {}
}
