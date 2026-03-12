package de.rsev.minecraft.forge.controller.logging;

public class ForgeLoggerBridge implements ForgeLoggerAdapter {

    @Override
    public void info(String message, Object... args) {
        ModLogger.info(message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        ModLogger.warn(message, args);
    }

    @Override
    public void error(String message, Object... args) {
        ModLogger.error(message, args);
    }

    @Override
    public void error(String message, Throwable throwable) {
        ModLogger.error(message, throwable);
    }
}