// This is an Adapter, 
// to exchange Data from different Forge repos to the common repos

package de.rsev.minecraft.forge.controller.logging;

public interface ForgeLoggerAdapter {

    void info(String message, Object... args);

    void warn(String message, Object... args);

    void error(String message, Object... args);

    void error(String message, Throwable throwable);
}