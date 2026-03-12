package de.rsev.minecraft.forge.controller.lua.tools.datapack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataPackWriter {

    public static void write(Path target, String relativePath, String content) throws IOException {
        Path file = target.resolve(relativePath);
        Files.createDirectories(file.getParent());
        Files.writeString(file, content);
    }

    private DataPackWriter() {}
}
