package de.rsev.minecraft.forge.controller.lua.tools.datapack;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;

import java.io.IOException;
import java.nio.file.Path;

public class LuaDataPackBuilder {

    private final Path targetRoot;

    public LuaDataPackBuilder(Path targetRoot) {
        this.targetRoot = targetRoot;
    }

    public void writeLootTable(String namespace, String path, String json) {
        try {
            String relative = "data/%s/loot_tables/%s.json".formatted(namespace, path);
            DataPackWriter.write(targetRoot, relative, json);
        } catch (IOException e) {
            LuaLogger.forgeError("Failed to write loot table {}", path, e);
        }
    }
}
