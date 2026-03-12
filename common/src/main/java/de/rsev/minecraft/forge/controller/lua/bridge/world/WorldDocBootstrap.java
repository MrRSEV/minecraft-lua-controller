package de.rsev.minecraft.forge.controller.lua.bridge.world;

import de.rsev.minecraft.forge.controller.lua.tools.documentation.*;

public class WorldDocBootstrap {

    private static boolean INITIALIZED = false;

    public static void init() {

        if (INITIALIZED) return;
        INITIALIZED = true;

        LuaDocumentationRegistry.register(LuaDocModule.WORLD, new LuaMethodDoc(
                        "World.blocks.set",
                        "World.blocks.set(x, y, z, blockId)",
                        "Setzt einen Block an einer Position.",
                        """
                        World.blocks.set(10, 64, 10, "minecraft:stone")
                        """,
                        "boolean"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.WORLD, new LuaMethodDoc(
                        "World.blocks.get",
                        "World.blocks.get(x, y, z)",
                        "Gibt den Blocknamen zurück.",
                        """
                        local block = World.blocks.get(10, 64, 10)
                        System.print(block)
                        """,
                        "string"
                )
        );
    }

    private WorldDocBootstrap() {}
}