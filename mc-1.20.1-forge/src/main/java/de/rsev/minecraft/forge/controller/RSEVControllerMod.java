package de.rsev.minecraft.forge.controller;

import com.mojang.logging.LogUtils;

import de.rsev.minecraft.forge.controller.config.ForgeConfigProvider;
import de.rsev.minecraft.forge.controller.config.LuaConfigBridge;
import de.rsev.minecraft.forge.controller.logging.ForgeLoggerBridge;
import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import de.rsev.minecraft.forge.controller.lua.bridge.inventory.ForgeLuaInventoryAdapter;
import de.rsev.minecraft.forge.controller.lua.bridge.inventory.LuaInventoryBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.player.ForgeLuaPlayerAdapter;
import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerAPI;
import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.scoreboard.ForgeLuaScoreboardAdapter;
import de.rsev.minecraft.forge.controller.lua.bridge.scoreboard.LuaScoreboardBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.world.ForgeLuaEntityAdapter;
import de.rsev.minecraft.forge.controller.lua.bridge.world.ForgeLuaWorldAdapter;
import de.rsev.minecraft.forge.controller.lua.bridge.world.LuaBlockAPI;
import de.rsev.minecraft.forge.controller.lua.bridge.world.LuaEntityAPI;
import de.rsev.minecraft.forge.controller.lua.bridge.world.LuaWorldBridge;
import de.rsev.minecraft.forge.controller.lua.tools.documentation.ForgeLuaDocsPathProvider;
import de.rsev.minecraft.forge.controller.lua.tools.documentation.LuaDocumentationBuilder;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;

import org.slf4j.Logger;
@Mod(RSEVControllerMod.MODID)
public class RSEVControllerMod {

    public static final String MODID = "minecraft_lua_controller";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static String getDataDirectoryName() {
        return MODID.replace('_', '-');
    }

    public RSEVControllerMod() {
        
        LuaConfigBridge.setProvider(new ForgeConfigProvider());
        LuaLogger.setForgeLogger(new ForgeLoggerBridge());
        LuaInventoryBridge.setAdapter(new ForgeLuaInventoryAdapter());
        LuaPlayerAPI.setAdapter(new ForgeLuaPlayerAdapter());
        LuaScoreboardBridge.setAdapter(new ForgeLuaScoreboardAdapter());
        LuaDocumentationBuilder.setPathProvider(new ForgeLuaDocsPathProvider());
        LuaBlockAPI.setAdapter(new ForgeLuaWorldAdapter());
        LuaEntityAPI.setAdapter(new ForgeLuaEntityAdapter());
        LuaWorldBridge.setAdapter(new ForgeLuaWorldAdapter());
        LuaPlayerBridge.setAdapter(new ForgeLuaPlayerAdapter());
        
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.register(this);

        LOGGER.info("RSEV Controller Mod initialized");
    }
}
