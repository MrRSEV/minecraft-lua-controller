package de.rsev.minecraft.forge.controller.paper.io;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PaperLuaDirectoryManager {

    public static void prepareRuntimeDirectories(JavaPlugin plugin, World world) throws IOException {
        Files.createDirectories(PaperLuaPathResolver.getLuaRoot(plugin));
        Files.createDirectories(PaperLuaPathResolver.getLogDir(plugin));
        Files.createDirectories(PaperLuaPathResolver.getLibsDir(plugin));

        Path worldDir = PaperLuaPathResolver.getWorldDir(plugin, world);
        Files.createDirectories(worldDir);
        Files.createDirectories(worldDir.resolve("events"));
        Files.createDirectories(worldDir.resolve("commands"));
    }

    private PaperLuaDirectoryManager() {}
}
