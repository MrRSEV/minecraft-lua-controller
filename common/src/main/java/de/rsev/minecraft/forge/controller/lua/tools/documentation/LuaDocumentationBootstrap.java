package de.rsev.minecraft.forge.controller.lua.tools.documentation;

public class LuaDocumentationBootstrap {

    private static boolean INITIALIZED = false;

    public static void init() {

        if (INITIALIZED) return;
        INITIALIZED = true;

        // Delegation an Bridge-Bootstraps
        de.rsev.minecraft.forge.controller.lua.bridge.world.WorldDocBootstrap.init();
        de.rsev.minecraft.forge.controller.lua.bridge.timer.TimerDocBootstrap.init();
        de.rsev.minecraft.forge.controller.lua.bridge.player.PlayerDocBootstrap.init();
        de.rsev.minecraft.forge.controller.lua.bridge.system.SystemDocBootstrap.init();
        de.rsev.minecraft.forge.controller.lua.bridge.inventory.InventoryDocBootstrap.init();
        de.rsev.minecraft.forge.controller.lua.bridge.commands.CommandsDocBootstrap.init();
        de.rsev.minecraft.forge.controller.lua.bridge.config.ConfigDocBootstrap.init();
        de.rsev.minecraft.forge.controller.lua.bridge.events.EventsDocBootstrap.init();
        de.rsev.minecraft.forge.controller.lua.bridge.io.IODocBootstrap.init();
        de.rsev.minecraft.forge.controller.lua.bridge.scoreboard.ScoreboardDocBootstrap.init();
        de.rsev.minecraft.forge.controller.lua.bridge.assets.AssetsDocBootstrap.init();

    }

    private LuaDocumentationBootstrap() {}
}
