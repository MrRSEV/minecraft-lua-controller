package de.rsev.minecraft.forge.controller.paper;

import de.rsev.minecraft.forge.controller.config.LuaConfigBridge;
import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import de.rsev.minecraft.forge.controller.lua.LuaRuntime;
import de.rsev.minecraft.forge.controller.lua.bridge.inventory.LuaInventoryBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerAPI;
import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.scoreboard.LuaScoreboardBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.world.LuaBlockAPI;
import de.rsev.minecraft.forge.controller.lua.bridge.world.LuaEntityAPI;
import de.rsev.minecraft.forge.controller.lua.bridge.world.LuaWorldBridge;
import de.rsev.minecraft.forge.controller.lua.tools.documentation.LuaDocumentationBuilder;
import de.rsev.minecraft.forge.controller.lua.tools.documentation.LuaDocumentationBootstrap;
import de.rsev.minecraft.forge.controller.paper.config.PaperConfigProvider;
import de.rsev.minecraft.forge.controller.paper.io.PaperLuaDirectoryManager;
import de.rsev.minecraft.forge.controller.paper.logging.PaperLoggerBridge;
import de.rsev.minecraft.forge.controller.paper.lua.PaperLuaPlatformContext;
import de.rsev.minecraft.forge.controller.paper.lua.PaperLuaServerAdapter;
import de.rsev.minecraft.forge.controller.paper.lua.bridge.inventory.PaperLuaInventoryAdapter;
import de.rsev.minecraft.forge.controller.paper.lua.bridge.player.PaperLuaPlayerAdapter;
import de.rsev.minecraft.forge.controller.paper.lua.bridge.scoreboard.PaperLuaScoreboardAdapter;
import de.rsev.minecraft.forge.controller.paper.lua.bridge.world.PaperLuaEntityAdapter;
import de.rsev.minecraft.forge.controller.paper.lua.bridge.world.PaperLuaWorldAdapter;
import de.rsev.minecraft.forge.controller.paper.lua.tools.documentation.PaperLuaDocsPathProvider;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class RSEVControllerPaperPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        World primaryWorld = Bukkit.getWorlds().isEmpty() ? null : Bukkit.getWorlds().get(0);
        if (primaryWorld == null) {
            getLogger().severe("No loaded world available for Lua runtime");
            return;
        }

        try {
            PaperLuaDirectoryManager.prepareRuntimeDirectories(this, primaryWorld);
        } catch (Exception e) {
            getLogger().severe("Failed to prepare Paper Lua directories: " + e.getMessage());
            return;
        }

        PaperLuaPlatformContext.bind(this, primaryWorld);
        LuaRuntime.setPlatformContext(new PaperLuaPlatformContext());
        LuaRuntime.setServerAdapter(new PaperLuaServerAdapter(getServer()));

        LuaConfigBridge.setProvider(new PaperConfigProvider(this));
        LuaLogger.setForgeLogger(new PaperLoggerBridge(getLogger()));
        LuaInventoryBridge.setAdapter(new PaperLuaInventoryAdapter());
        LuaPlayerAPI.setAdapter(new PaperLuaPlayerAdapter());
        LuaPlayerBridge.setAdapter(new PaperLuaPlayerAdapter());
        LuaScoreboardBridge.setAdapter(new PaperLuaScoreboardAdapter());
        LuaBlockAPI.setAdapter(new PaperLuaWorldAdapter());
        LuaEntityAPI.setAdapter(new PaperLuaEntityAdapter());
        LuaWorldBridge.setAdapter(new PaperLuaWorldAdapter());
        LuaDocumentationBuilder.setPathProvider(new PaperLuaDocsPathProvider(this));

        LuaRuntime.initialize();
        LuaDocumentationBootstrap.init();
        LuaRuntime.onWorldLoad();

        getLogger().info("RSEV Controller Paper plugin enabled");
    }

    @Override
    public void onDisable() {
        LuaRuntime.onWorldUnload();
        getLogger().info("RSEV Controller Paper plugin disabled");
    }
}
