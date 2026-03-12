package de.rsev.minecraft.forge.controller.paper.logging;

import de.rsev.minecraft.forge.controller.logging.ForgeLoggerAdapter;

import java.util.logging.Logger;

public class PaperLoggerBridge implements ForgeLoggerAdapter {

    private final Logger logger;

    public PaperLoggerBridge(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String message, Object... args) {
        logger.info(format(message, args));
    }

    @Override
    public void warn(String message, Object... args) {
        logger.warning(format(message, args));
    }

    @Override
    public void error(String message, Object... args) {
        logger.severe(format(message, args));
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.severe(message + " :: " + throwable.getMessage());
    }

    private String format(String message, Object... args) {
        String formatted = message;
        if (args != null) {
            for (Object arg : args) {
                formatted = formatted.replaceFirst("\\{}", arg == null ? "null" : arg.toString());
            }
        }
        return formatted;
    }
}
