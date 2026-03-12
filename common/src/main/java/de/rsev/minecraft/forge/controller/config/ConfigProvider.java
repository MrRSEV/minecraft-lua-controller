// This is an Providing Bridge, 
// to exchange Data from different Forge repos to the common repos

package de.rsev.minecraft.forge.controller.config;

public interface ConfigProvider {

    boolean isLuaEnabled();

    int getLuaTickBudget();

    void setLuaEnabled(boolean value);

    void setLuaTickBudget(int value);
}