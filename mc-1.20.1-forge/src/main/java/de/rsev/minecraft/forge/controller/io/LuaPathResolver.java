package de.rsev.minecraft.forge.controller.io;

import de.rsev.minecraft.forge.controller.RSEVControllerMod;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class LuaPathResolver {

    public static Path getLuaRoot() {
        return FMLPaths.MODSDIR.get()
                .resolve(RSEVControllerMod.getDataDirectoryName())
                .resolve("lua");
    }

    public static Path getLoadingDir() {
        return getLuaRoot().resolve("loading");
    }

    public static Path getLogDir() {
        return getLuaRoot().resolve("log");
    }

    public static Path getLibsDir() {
        return getLuaRoot().resolve("libs");
    }

    public static Path getGeneratedDir() {
        return getLuaRoot().resolve("generated");
    }

    public static Path getGeneratedDataPacksDir() {
        return getGeneratedDir().resolve("datapacks");
    }

    public static Path getWorldDir(String worldName) {
        return getLuaRoot()
                .resolve("worlds")
                .resolve(worldName);
    }


    public static String resolveWorldName(MinecraftServer server) {
        return server.getWorldData().getLevelName();
    }


}
