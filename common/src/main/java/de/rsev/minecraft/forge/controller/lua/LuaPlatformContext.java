package de.rsev.minecraft.forge.controller.lua;

import java.nio.file.Path;

/**
 * Plattformneutraler Kontext fuer Lua-Pfade und Weltbindung.
 * Wird von Forge, spaeter auch von Paper, bereitgestellt.
 */
public interface LuaPlatformContext {

    Path getCurrentWorldDir();

    Path getLuaRootDir();

    Path getLuaGlobalDir();

    Path getLuaWorldDir();

    Path getLuaLogDir();

    default Path getLuaLibsDir() {
        return null;
    }

    default Path getLuaLoadingDir() {
        return null;
    }
}
