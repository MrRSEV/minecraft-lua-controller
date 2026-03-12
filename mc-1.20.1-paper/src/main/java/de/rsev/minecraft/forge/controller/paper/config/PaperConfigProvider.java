package de.rsev.minecraft.forge.controller.paper.config;

import de.rsev.minecraft.forge.controller.config.ConfigProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperConfigProvider implements ConfigProvider {

    private final JavaPlugin plugin;

    public PaperConfigProvider(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    @Override
    public boolean isLuaEnabled() {
        return plugin.getConfig().getBoolean("lua.enabled", true);
    }

    @Override
    public int getLuaTickBudget() {
        return plugin.getConfig().getInt("lua.tickBudget", 50);
    }

    @Override
    public void setLuaEnabled(boolean value) {
        plugin.getConfig().set("lua.enabled", value);
        plugin.saveConfig();
    }

    @Override
    public void setLuaTickBudget(int value) {
        plugin.getConfig().set("lua.tickBudget", value);
        plugin.saveConfig();
    }
}
