package de.rsev.minecraft.forge.controller.lua.bridge.io;

import de.rsev.minecraft.forge.controller.lua.LuaRuntime;

import java.nio.file.Path;

public final class LuaPathSandbox {

    private static final String DEFAULT_MOD_ID = "minecraft_lua_controller";
    private static final Path DEFAULT_ROOT = Path.of("mods", DEFAULT_MOD_ID.replace('_', '-'), "lua")
            .toAbsolutePath()
            .normalize();

    public static Path resolve(String inputPath) {
        if (inputPath == null || inputPath.isBlank()) {
            throw new IllegalArgumentException("Path is null/blank");
        }

        Path requested = Path.of(inputPath)
                .toAbsolutePath()
                .normalize();

        if (!requested.startsWith(root())) {
            throw new SecurityException("Access outside Lua sandbox: " + inputPath);
        }

        if (isBlocked(requested)) {
            throw new SecurityException("Blocked system path: " + inputPath);
        }

        return requested;
    }

    private static boolean isBlocked(Path path) {
        String p = path.toString().toLowerCase();

        if (p.contains("windows") || p.contains("system32")) {
            return true;
        }

        if (p.startsWith("/etc") || p.startsWith("/bin") || p.startsWith("/usr")) {
            return true;
        }

        return p.startsWith("/system") || p.startsWith("/applications");
    }

    public static Path root() {
        Path runtimeRoot = LuaRuntime.getLuaRootDir();
        return runtimeRoot != null ? runtimeRoot.toAbsolutePath().normalize() : DEFAULT_ROOT;
    }

    private LuaPathSandbox() {}
}
