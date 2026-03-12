package de.rsev.minecraft.forge.controller.paper.lua.tools.documentation;

import de.rsev.minecraft.forge.controller.lua.tools.documentation.LuaDocsPathProvider;
import de.rsev.minecraft.forge.controller.paper.io.PaperLuaPathResolver;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

public class PaperLuaDocsPathProvider implements LuaDocsPathProvider {

    private final JavaPlugin plugin;

    public PaperLuaDocsPathProvider(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Path getDocsDirectory() {
        return PaperLuaPathResolver.getLuaRoot(plugin).resolve("docs");
    }
}
