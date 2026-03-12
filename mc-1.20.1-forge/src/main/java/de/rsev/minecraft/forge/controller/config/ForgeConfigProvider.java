// This is an providing adapter, 
// to exchange data from different forge repos to the common repos

package de.rsev.minecraft.forge.controller.config;

public class ForgeConfigProvider implements ConfigProvider {

    @Override
    public boolean isLuaEnabled() {
        return ControllerConfig.ENABLE_LUA.get();
    }

    @Override
    public int getLuaTickBudget() {
        return ControllerConfig.LUA_TICK_BUDGET.get();
    }

    @Override
    public void setLuaEnabled(boolean value) {
        ControllerConfig.ENABLE_LUA.set(value);
    }

    @Override
    public void setLuaTickBudget(int value) {
        ControllerConfig.LUA_TICK_BUDGET.set(value);
    }
}