package de.rsev.minecraft.forge.controller.config;

public class LuaConfigBridge {

    private static ConfigProvider provider;

    public static void setProvider(ConfigProvider p) {
        provider = p;
    }

    public static boolean isLuaEnabled() {
        return provider.isLuaEnabled();
    }

    public static int getLuaTickBudget() {
        return provider.getLuaTickBudget();
    }

    public static void setLuaEnabled(boolean value) {
        provider.setLuaEnabled(value);
    }

    public static void setLuaTickBudget(int value) {
        provider.setLuaTickBudget(value);
    }

}