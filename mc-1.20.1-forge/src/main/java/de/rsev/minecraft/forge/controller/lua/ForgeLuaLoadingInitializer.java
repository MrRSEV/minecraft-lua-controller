package de.rsev.minecraft.forge.controller.lua;

import de.rsev.minecraft.forge.controller.logging.ModLogger;
import de.rsev.minecraft.forge.controller.lua.script.discovery.LuaScriptDiscovery;
import de.rsev.minecraft.forge.controller.lua.vm.types.LoadingVM;

import java.nio.file.Path;

/**
 * Forge-internes Bootstrap fuer Loading-VMs.
 * Common kennt nur den VM-Typ, nicht dessen Plattforminitialisierung.
 */
public final class ForgeLuaLoadingInitializer {

    public static void initialize() {
        Path loadingDir = LuaRuntime.getLuaLoadingDir();

        if (loadingDir == null) {
            ModLogger.warn("Skipping loading VM initialization: loadingDir is null");
            return;
        }

        try {
            ForgeLuaAssetLoader.prepareGeneratedDataPacks();
            LoadingVM loadingVm = new LoadingVM();
            LuaScriptDiscovery.discover(loadingDir).forEach(loadingVm::executeScript);
            ModLogger.info("Loading VM initialized from {}", loadingDir);
        } catch (Exception e) {
            ModLogger.error("Failed to initialize loading VM from " + loadingDir, e);
        }
    }

    private ForgeLuaLoadingInitializer() {}
}
