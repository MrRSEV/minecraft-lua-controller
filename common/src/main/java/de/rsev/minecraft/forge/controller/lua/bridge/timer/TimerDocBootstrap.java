package de.rsev.minecraft.forge.controller.lua.bridge.timer;

import de.rsev.minecraft.forge.controller.lua.tools.documentation.*;

public class TimerDocBootstrap {

    private static boolean INITIALIZED = false;

    public static void init() {

        if (INITIALIZED) return;
        INITIALIZED = true;

        LuaDocumentationRegistry.register(LuaDocModule.TIMER, new LuaMethodDoc(
                        "Timer.after",
                        "Timer.after(ticks, function)",
                        "Führt eine Funktion verzögert aus.",
                        """
                        Timer.after(100, function()
                            System.print("100 Ticks später")
                        end)
                        """,
                        "taskId"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.TIMER, new LuaMethodDoc(
                        "Timer.every",
                        "Timer.every(ticks, function)",
                        "Führt eine Funktion wiederholt aus.",
                        """
                        Timer.every(20, function()
                            System.print("Jede Sekunde")
                        end)
                        """,
                        "taskId"
                )
        );
    }

    private TimerDocBootstrap() {}
}