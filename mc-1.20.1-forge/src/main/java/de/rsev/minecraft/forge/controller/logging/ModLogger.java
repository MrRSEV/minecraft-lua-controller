package de.rsev.minecraft.forge.controller.logging;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class ModLogger {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static void info(String message, Object... args) {
        LOGGER.info(message, args);
    }

    public static void warn(String message, Object... args) {
        LOGGER.warn(message, args);
    }

    public static void error(String message, Object... args) {
        LOGGER.error(message, args);
    }

    public static void error(String message, Throwable throwable) {
        LOGGER.error(message, throwable);
    }

    private ModLogger() {}
}
