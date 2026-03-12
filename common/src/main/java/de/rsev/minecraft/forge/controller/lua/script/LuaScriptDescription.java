package de.rsev.minecraft.forge.controller.lua.script;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class LuaScriptDescription {

    private final Path path;
    private final Set<String> permissions = new HashSet<>();

    public LuaScriptDescription(Path path) {
        this.path = path;
    }

    public Path getPath() {
        return path;
    }

    public Set<String> getPermissions() {
        return permissions;
    }
}
