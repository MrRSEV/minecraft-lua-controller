package de.rsev.minecraft.forge.controller.lua;

import de.rsev.minecraft.forge.controller.RSEVControllerMod;
import de.rsev.minecraft.forge.controller.io.LuaPathResolver;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public final class ForgeLuaAssetLoader {

    private static final String MARKER_FILE = ".minecraft-lua-controller";

    public static void prepareGeneratedDataPacks() throws IOException {
        Files.createDirectories(LuaPathResolver.getGeneratedDataPacksDir());
    }

    public static void syncGeneratedDataPacks(MinecraftServer server) throws IOException {
        Path sourceRoot = LuaPathResolver.getGeneratedDataPacksDir();
        Path targetRoot = server.getWorldPath(LevelResource.DATAPACK_DIR);

        Files.createDirectories(sourceRoot);
        Files.createDirectories(targetRoot);

        Set<String> sourcePacks = new HashSet<>();

        try (var stream = Files.list(sourceRoot)) {
            stream.filter(Files::isDirectory).forEach(sourcePack -> {
                sourcePacks.add(sourcePack.getFileName().toString());
                Path targetPack = targetRoot.resolve(sourcePack.getFileName().toString());

                try {
                    copyDirectory(sourcePack, targetPack);
                    RSEVControllerMod.LOGGER.info("Synced Lua datapack {}", sourcePack.getFileName());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException ioException) {
                throw ioException;
            }
            throw e;
        }

        try (var stream = Files.list(targetRoot)) {
            stream.filter(Files::isDirectory)
                    .filter(path -> Files.exists(path.resolve(MARKER_FILE)))
                    .filter(path -> !sourcePacks.contains(path.getFileName().toString()))
                    .forEach(path -> {
                        try {
                            deleteDirectory(path);
                            RSEVControllerMod.LOGGER.info("Removed stale Lua datapack {}", path.getFileName());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException ioException) {
                throw ioException;
            }
            throw e;
        }
    }

    private static void copyDirectory(Path source, Path target) throws IOException {
        deleteDirectory(target);

        try (var stream = Files.walk(source)) {
            stream.forEach(path -> {
                try {
                    Path relative = source.relativize(path);
                    Path targetPath = target.resolve(relative.toString());

                    if (Files.isDirectory(path)) {
                        Files.createDirectories(targetPath);
                    } else {
                        Files.createDirectories(targetPath.getParent());
                        Files.copy(path, targetPath);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException ioException) {
                throw ioException;
            }
            throw e;
        }
    }

    private static void deleteDirectory(Path root) throws IOException {
        if (!Files.exists(root)) {
            return;
        }

        try (var stream = Files.walk(root)) {
            stream.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            if (e.getCause() instanceof IOException ioException) {
                throw ioException;
            }
            throw e;
        }
    }

    private ForgeLuaAssetLoader() {}
}
