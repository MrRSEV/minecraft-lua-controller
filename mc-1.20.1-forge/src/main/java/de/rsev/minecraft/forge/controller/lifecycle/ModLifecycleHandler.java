package de.rsev.minecraft.forge.controller.lifecycle;

import de.rsev.minecraft.forge.controller.RSEVControllerMod;
import de.rsev.minecraft.forge.controller.io.LuaDirectoryManager;
import de.rsev.minecraft.forge.controller.lua.ForgeLuaLoadingInitializer;
import de.rsev.minecraft.forge.controller.lua.ForgeLuaPlatformContext;
import de.rsev.minecraft.forge.controller.lua.LuaRuntime;
import de.rsev.minecraft.forge.controller.lua.tools.documentation.LuaDocumentationBootstrap;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RSEVControllerMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModLifecycleHandler {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        try {
            LuaDirectoryManager.prepareLuaRoot();
            LuaDirectoryManager.prepareLogDir();
            LuaDirectoryManager.prepareLoadingDir();
            LuaDirectoryManager.mirrorLoadingFromJar();
        } catch (Exception e) {
            RSEVControllerMod.LOGGER.error("Failed preparing Forge Lua bootstrap directories", e);
        }

        ForgeLuaPlatformContext.initializeBasePaths();
        LuaRuntime.setPlatformContext(ForgeLuaPlatformContext.create());
        LuaRuntime.initialize();
        ForgeLuaLoadingInitializer.initialize();
        LuaDocumentationBootstrap.init();
        RSEVControllerMod.LOGGER.info("Common setup completed");
    }
}
