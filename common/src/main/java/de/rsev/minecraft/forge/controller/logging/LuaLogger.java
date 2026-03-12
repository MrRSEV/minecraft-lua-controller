package de.rsev.minecraft.forge.controller.logging;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LuaLogger {

    private static Path LOG_DIR;
    private static Path LATEST_LOG;

    private static ForgeLoggerAdapter forge;

    private static final DateTimeFormatter TS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // =========================
    // Forge Adapter Injection
    // =========================

    public static void setForgeLogger(ForgeLoggerAdapter adapter) {
        forge = adapter;
    }

    // =========================
    // Forge Logger Methods
    // =========================

    public static void forgeInfo(String message, Object... args) {
        if (forge != null) forge.info(message, args);
    }

    public static void forgeWarn(String message, Object... args) {
        if (forge != null) forge.warn(message, args);
    }

    public static void forgeError(String message, Object... args) {
        if (forge != null) forge.error(message, args);
    }

    public static void forgeError(String message, Throwable t) {
        if (forge != null) forge.error(message, t);
    }

    // =========================
    // Init / Shutdown
    // =========================

    public static void init(Path luaRoot) {
        try {
            LOG_DIR = luaRoot;
            Files.createDirectories(LOG_DIR);

            LATEST_LOG = LOG_DIR.resolve("latest.log");

            if (!Files.exists(LATEST_LOG)) {
                Files.createFile(LATEST_LOG);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void shutdown() {
        try {
            if (LATEST_LOG != null && Files.exists(LATEST_LOG)) {

                String ts = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));

                Path rotated = LOG_DIR.resolve(ts + ".log");

                Files.move(LATEST_LOG, rotated, StandardCopyOption.REPLACE_EXISTING);
                Files.createFile(LATEST_LOG);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // LUA FILE LOGGER (unverändert)
    // =========================

    public static void info(String message, Object... args) {
        log("INFO", format(message, args));
    }

    public static void warn(String message, Object... args) {
        log("WARN", format(message, args));
    }

    public static void error(String message, Object... args) {
        log("ERROR", format(message, args));
    }

    public static void error(String message, Throwable t) {
        log("ERROR", message + " \n → " + t);
    }

    public static void debug(String message, Object... args) {
        log("DEBUG", format(message, args));
    }

    // =========================
    // Core Log Writer
    // =========================

    private static void log(String level, String msg) {

        if (LATEST_LOG == null) return;

        String line = String.format(
                "[%s] [%s] %s%n",
                LocalDateTime.now().format(TS),
                level,
                msg
        );

        try {
            Files.writeString(
                    LATEST_LOG,
                    line,
                    StandardOpenOption.APPEND
            );

            System.out.print("[LUA][" + level + "] " + msg + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // {} Formatter
    // =========================

    private static String format(String message, Object... args) {

        if (args == null || args.length == 0) {
            return message;
        }

        String formatted = message;

        for (Object arg : args) {
            formatted = formatted.replaceFirst("\\{}", argToString(arg));
        }

        return formatted;
    }

    private static String argToString(Object obj) {
        return (obj == null) ? "null" : obj.toString();
    }

    private LuaLogger() {}
}