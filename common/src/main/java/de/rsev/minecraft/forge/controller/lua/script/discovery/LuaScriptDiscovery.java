package de.rsev.minecraft.forge.controller.lua.script.discovery;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class LuaScriptDiscovery {

    public static List<Path> discover(Path root) throws IOException {
        try (Stream<Path> stream = Files.walk(root)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".lua"))
                    .sorted()
                    .toList();
        }
    }

    private LuaScriptDiscovery() {}
}
