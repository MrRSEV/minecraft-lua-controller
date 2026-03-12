package de.rsev.minecraft.forge.controller.lua;

import de.rsev.minecraft.forge.controller.io.LuaPathResolver;

import java.nio.file.Path;

/**
 * Forge-spezifische Quelle fuer Lua-Pfade und Weltbindung.
 */
public final class ForgeLuaPlatformContext implements LuaPlatformContext {

    private static Path currentWorldDir;
    private static Path luaRootDir;
    private static Path luaGlobalDir;
    private static Path luaWorldDir;
    private static Path luaLogDir;
    private static Path luaLibsDir;
    private static Path luaLoadingDir;

    public static void initializeBasePaths() {
        luaRootDir = LuaPathResolver.getLuaRoot();
        luaGlobalDir = luaRootDir;
        luaLogDir = LuaPathResolver.getLogDir();
        luaLibsDir = LuaPathResolver.getLibsDir();
        luaLoadingDir = LuaPathResolver.getLoadingDir();
    }

    public static void bind(Path worldDir) {
        initializeBasePaths();
        setCurrentWorldDir(worldDir);
    }

    public static void setCurrentWorldDir(Path worldDir) {
        currentWorldDir = worldDir;
        luaWorldDir = worldDir != null
                ? LuaPathResolver.getWorldDir(worldDir.getFileName().toString())
                : null;
    }

    public static ForgeLuaPlatformContext create() {
        return new ForgeLuaPlatformContext();
    }

    @Override
    public Path getCurrentWorldDir() {
        return currentWorldDir;
    }

    @Override
    public Path getLuaRootDir() {
        return luaRootDir;
    }

    @Override
    public Path getLuaGlobalDir() {
        return luaGlobalDir;
    }

    @Override
    public Path getLuaWorldDir() {
        return luaWorldDir;
    }

    @Override
    public Path getLuaLogDir() {
        return luaLogDir;
    }

    @Override
    public Path getLuaLibsDir() {
        return luaLibsDir;
    }

    @Override
    public Path getLuaLoadingDir() {
        return luaLoadingDir;
    }

    private ForgeLuaPlatformContext() {}
}
