package de.rsev.minecraft.forge.controller.lua.bridge.scoreboard;

import de.rsev.minecraft.forge.controller.lua.tools.documentation.LuaDocModule;
import de.rsev.minecraft.forge.controller.lua.tools.documentation.LuaDocumentationRegistry;
import de.rsev.minecraft.forge.controller.lua.tools.documentation.LuaMethodDoc;

public class ScoreboardDocBootstrap {
        
    private static boolean INITIALIZED = false;

    public static void init() {

        if (INITIALIZED) return;
        INITIALIZED = true;
    
        // =========================
        // Scoreboard.set
        // =========================
        
        LuaDocumentationRegistry.register(LuaDocModule.SCOREBOARD, new LuaMethodDoc(
            "Scoreboard.set",
            "Scoreboard.set(objective, playerId, value)",
            "Setzt den Score eines Spielers. Ist noch nicht vollständig nutzbar, wird in einer der nächsten Minor Relases nachgeliefert",
            "Scoreboard.set(\"kills\", 1, 5)",
            "boolean"
        ));

    }

}
