package de.rsev.minecraft.forge.controller.lua.bridge.system;

import de.rsev.minecraft.forge.controller.lua.tools.documentation.*;

public class SystemDocBootstrap {

    private static boolean INITIALIZED = false;

    public static void init() {

        if (INITIALIZED) return;
        INITIALIZED = true;

        // =========================
        // System.print
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.SYSTEM, new LuaMethodDoc(
                        "System.print",
                        "System.print(message)",
                        "Schreibt eine Info-Nachricht ins Lua Log.",
                        """
                        System.print("Hallo Welt")
                        """,
                        "nil"
                )
        );

        // =========================
        // System.warn
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.SYSTEM, new LuaMethodDoc(
                        "System.warn",
                        "System.warn(message)",
                        "Schreibt eine Warnung ins Lua Log.",
                        """
                        System.warn("Achtung!")
                        """,
                        "nil"
                )
        );

        // =========================
        // System.error
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.SYSTEM, new LuaMethodDoc(
                        "System.error",
                        "System.error(message)",
                        "Schreibt eine Fehlermeldung ins Lua Log.",
                        """
                        System.error("Etwas ist schiefgelaufen")
                        """,
                        "nil"
                )
        );

        // =========================
        // System.debug
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.SYSTEM, new LuaMethodDoc(
                        "System.debug",
                        "System.debug(message)",
                        "Schreibt eine Debug-Nachricht (nur DEV_MODE sichtbar).",
                        """
                        System.debug("Variable x = " .. x)
                        """,
                        "nil"
                )
        );

        // =========================
        // System.currentTimeMillis
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.SYSTEM, new LuaMethodDoc(
                        "System.currentTimeMillis",
                        "System.currentTimeMillis()",
                        "Gibt die aktuelle Systemzeit in Millisekunden zurück.",
                        """
                        local time = System.currentTimeMillis()
                        System.print(time)
                        """,
                        "number"
                )
        );
    }

    private SystemDocBootstrap() {}
}