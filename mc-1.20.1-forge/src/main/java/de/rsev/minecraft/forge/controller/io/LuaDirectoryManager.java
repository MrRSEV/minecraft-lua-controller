package de.rsev.minecraft.forge.controller.io;

import de.rsev.minecraft.forge.controller.RSEVControllerMod;
import de.rsev.minecraft.forge.controller.logging.ModLogger;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.*;
import java.util.stream.Stream;

/**
 * Verwaltet Verzeichnisse und Standarddateien für Lua-Skripte.
 * Zuständig für:
 * - Anlegen benötigter Ordner
 * - Spiegeln (Mirror) von Lua-Standard-Skripten aus der Mod-JAR
 */
public class LuaDirectoryManager {

    /**
     * Stellt sicher, dass das Lua-Hauptverzeichnis existiert.
     *
     * @return Pfad zum Lua-Root-Verzeichnis
     * @throws IOException falls das Verzeichnis nicht erstellt werden kann
     */
    public static Path prepareLuaRoot() throws IOException {
        Path luaRoot = LuaPathResolver.getLuaRoot();
        Files.createDirectories(luaRoot); // Erstellt Verzeichnis inkl. Elternordnern
        return luaRoot;
    }

    public static Path prepareLogDir() throws IOException {
        Path logDir = LuaPathResolver.getLogDir();
        Files.createDirectories(logDir);
        return logDir;
    }

    public static Path prepareLibsDir() throws IOException {
        Path libsDir = LuaPathResolver.getLibsDir();
        Files.createDirectories(libsDir);
        return libsDir;
    }

    public static Path prepareGeneratedDir() throws IOException {
        Path generatedDir = LuaPathResolver.getGeneratedDir();
        Files.createDirectories(generatedDir);
        return generatedDir;
    }

    public static Path prepareGeneratedDataPacksDir() throws IOException {
        Path generatedDataPacksDir = LuaPathResolver.getGeneratedDataPacksDir();
        Files.createDirectories(generatedDataPacksDir);
        return generatedDataPacksDir;
    }

    /**
     * Stellt sicher, dass das "loading"-Verzeichnis existiert.
     * Dieses enthält Lua-Skripte, die beim Laden verwendet werden.
     *
     * @return Pfad zum Loading-Verzeichnis
     * @throws IOException falls das Verzeichnis nicht erstellt werden kann
     */
    public static Path prepareLoadingDir() throws IOException {
        Path loadingDir = LuaPathResolver.getLoadingDir();
        Files.createDirectories(loadingDir);
        return loadingDir;
    }

    /**
     * Bereitet das welt-spezifische Lua-Verzeichnis vor.
     * Zusätzlich werden Unterordner für Events und Commands erstellt.
     *
     * @param server MinecraftServer-Instanz zur Ermittlung des Weltnamens
     * @return Pfad zum Weltverzeichnis
     * @throws IOException falls Verzeichnisse nicht erstellt werden können
     */
    public static Path prepareWorldDir(MinecraftServer server) throws IOException {
        // Weltname aus dem Server ermitteln
        String worldName = LuaPathResolver.resolveWorldName(server);
        ModLogger.info("Resolved world name: {}", worldName);

        // Zielverzeichnis bestimmen
        Path worldDir = LuaPathResolver.getWorldDir(worldName);

        // Hauptverzeichnis und Unterordner erstellen
        Files.createDirectories(worldDir);
        Files.createDirectories(worldDir.resolve("events"));
        Files.createDirectories(worldDir.resolve("commands"));

        return worldDir;
    }

    public static void prepareRuntimeDirectories(MinecraftServer server) throws IOException {
        prepareLuaRoot();
        prepareLogDir();
        prepareLibsDir();
        prepareGeneratedDir();
        prepareGeneratedDataPacksDir();
        prepareLoadingDir();
        prepareWorldDir(server);
    }

    /**
     * Spiegelt (kopiert) Standard-Lua-Skripte aus der Mod-JAR
     * in das Loading-Verzeichnis, falls diese dort noch nicht existieren.
     */
    public static void mirrorLoadingFromJar() {
        Path loadingDir = LuaPathResolver.getLoadingDir();

        try {
            // Sicherstellen, dass das Zielverzeichnis existiert
            Files.createDirectories(loadingDir);

            URI uri = resolveLoadingResourceUri();

            // Falls Ressource aus einer JAR stammt → neues FileSystem öffnen
            try (FileSystem fs = (uri.getScheme().equals("jar")
                    ? FileSystems.newFileSystem(uri, java.util.Map.of())
                    : null)) {

                // Pfad zur Ressource
                Path jarPath = Paths.get(uri);

                // Alle Dateien rekursiv durchlaufen
                try (Stream<Path> stream = Files.walk(jarPath)) {
                    stream
                        .filter(Files::isRegularFile) // Nur Dateien
                        .filter(p -> p.toString().endsWith(".lua")) // Nur Lua-Skripte
                        .forEach(source ->
                            copyIfAbsent(
                                source,
                                loadingDir.resolve(source.getFileName().toString())
                            )
                        );
                }
            }

            RSEVControllerMod.LOGGER.info("Default Lua loading scripts mirrored");

        } catch (Exception e) {
            // Fehler beim Zugriff auf Ressourcen / Kopieren
            RSEVControllerMod.LOGGER.error("Failed to mirror loading scripts", e);
        }
    }

    /**
     * Kopiert eine Datei nur dann, wenn sie im Ziel noch nicht existiert.
     *
     * @param source Quelldatei
     * @param target Zieldatei
     */
    private static void copyIfAbsent(Path source, Path target) {
        try {
            // Falls Datei bereits existiert → nichts tun
            if (Files.exists(target)) return;

            // Datei per Stream kopieren
            try (InputStream in = Files.newInputStream(source)) {
                Files.copy(in, target);
                RSEVControllerMod.LOGGER.info("Copied Lua script: {}", target.getFileName());
            }

        } catch (IOException e) {
            // Fehler beim Kopieren
            RSEVControllerMod.LOGGER.error("Failed copying {}", source.getFileName(), e);
        }
    }

    private static URI resolveLoadingResourceUri() throws Exception {
        var classLoader = LuaDirectoryManager.class.getClassLoader();
        var resource = classLoader.getResource("lua/loading");

        if (resource == null) {
            resource = classLoader.getResource("lua/load");
        }

        if (resource == null) {
            throw new IOException("Missing bundled loading scripts under lua/loading or lua/load");
        }

        return resource.toURI();
    }

    /**
     * Privater Konstruktor verhindert Instanziierung.
     * Utility-Klasse mit statischen Methoden.
     */
    private LuaDirectoryManager() {}
}
