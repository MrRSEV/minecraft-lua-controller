package de.rsev.minecraft.forge.controller.lua.bridge.assets;

import de.rsev.minecraft.forge.controller.lua.tools.documentation.LuaDocModule;
import de.rsev.minecraft.forge.controller.lua.tools.documentation.LuaDocumentationRegistry;
import de.rsev.minecraft.forge.controller.lua.tools.documentation.LuaMethodDoc;

public final class AssetsDocBootstrap {

    private static final String LOADING_VM_NOTE =
            "Only available for loading VMs. Use this API exclusively from scripts in lua/loading because the generated assets are meant to be built once during startup.";

    private static boolean INITIALIZED = false;

    public static void init() {
        if (INITIALIZED) return;
        INITIALIZED = true;

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.createPack",
                        "Assets.createPack(packName, description[, packFormat])",
                        "Creates or updates a generated datapack root. " + LOADING_VM_NOTE,
                        """
                        local packRoot = Assets.createPack(
                            "lua-example-assets",
                            "Assets generated during startup"
                        )

                        System.print(packRoot)
                        """,
                        "string"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.clearPack",
                        "Assets.clearPack(packName)",
                        "Removes a previously generated datapack folder. " + LOADING_VM_NOTE,
                        """
                        Assets.clearPack("lua-example-assets")
                        """,
                        "nil"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.write",
                        "Assets.write(packName, relativePath, content)",
                        "Writes an arbitrary file into a generated datapack. " + LOADING_VM_NOTE,
                        """
                        Assets.write(
                            "lua-example-assets",
                            "data/minecraft/functions/hello.mcfunction",
                            "say hello from lua"
                        )
                        """,
                        "nil"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.writeFunction",
                        "Assets.writeFunction(packName, target, content)",
                        "Writes a `.mcfunction` file using `namespace:path` notation. " + LOADING_VM_NOTE,
                        """
                        Assets.writeFunction(
                            "lua-example-assets",
                            "minecraft:lua_example_ping",
                            "say loaded from lua"
                        )
                        """,
                        "nil"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.writeLootTable",
                        "Assets.writeLootTable(packName, namespace, path, json)",
                        "Writes a loot table JSON file. " + LOADING_VM_NOTE,
                        """
                        local loot = Assets.simpleChestLoot("minecraft:diamond", 3)
                        Assets.writeLootTable(
                            "lua-example-assets",
                            "minecraft",
                            "chests/lua_example_bonus",
                            loot
                        )
                        """,
                        "nil"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.writeRecipe",
                        "Assets.writeRecipe(packName, namespace, path, json)",
                        "Writes a recipe JSON file. " + LOADING_VM_NOTE,
                        """
                        local recipe = Assets.shapelessRecipe(
                            "minecraft:golden_apple",
                            1,
                            "minecraft:apple",
                            "minecraft:gold_block"
                        )

                        Assets.writeRecipe(
                            "lua-example-assets",
                            "minecraft",
                            "golden_apple_from_block",
                            recipe
                        )
                        """,
                        "nil"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.writeAdvancement",
                        "Assets.writeAdvancement(packName, namespace, path, json)",
                        "Writes an advancement JSON file. " + LOADING_VM_NOTE,
                        """
                        local advancement = Assets.impossibleAdvancement(
                            "Lua Test",
                            "Generated during loading",
                            "minecraft:command_block"
                        )

                        Assets.writeAdvancement(
                            "lua-example-assets",
                            "minecraft",
                            "lua/generated_test",
                            advancement
                        )
                        """,
                        "nil"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.writePredicate",
                        "Assets.writePredicate(packName, namespace, path, json)",
                        "Writes a predicate JSON file. " + LOADING_VM_NOTE,
                        """
                        local predicate = Assets.predicateMatchTool("minecraft:diamond_pickaxe")

                        Assets.writePredicate(
                            "lua-example-assets",
                            "minecraft",
                            "tools/diamond_pickaxe",
                            predicate
                        )
                        """,
                        "nil"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.writeTag",
                        "Assets.writeTag(packName, registryType, namespace, path, json)",
                        "Writes a tag file under `data/<namespace>/tags/...`. " + LOADING_VM_NOTE,
                        """
                        local tag = Assets.itemTag(
                            "minecraft:diamond",
                            "minecraft:emerald"
                        )

                        Assets.writeTag(
                            "lua-example-assets",
                            "items",
                            "minecraft",
                            "lua/generated_gems",
                            tag
                        )
                        """,
                        "nil"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.simpleChestLoot",
                        "Assets.simpleChestLoot(itemId, count)",
                        "Creates a simple chest loot table JSON string. " + LOADING_VM_NOTE,
                        """
                        local json = Assets.simpleChestLoot("minecraft:diamond", 3)
                        System.print(json)
                        """,
                        "string"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.shapelessRecipe",
                        "Assets.shapelessRecipe(resultItem, resultCount, ingredient1, ...)",
                        "Creates a shapeless recipe JSON string. " + LOADING_VM_NOTE,
                        """
                        local recipe = Assets.shapelessRecipe(
                            "minecraft:bread",
                            1,
                            "minecraft:wheat",
                            "minecraft:wheat",
                            "minecraft:wheat"
                        )
                        """,
                        "string"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.smeltingRecipe",
                        "Assets.smeltingRecipe(inputItem, resultItem, experience[, cookingTime])",
                        "Creates a smelting recipe JSON string. " + LOADING_VM_NOTE,
                        """
                        local recipe = Assets.smeltingRecipe(
                            "minecraft:iron_ore",
                            "minecraft:iron_ingot",
                            0.7,
                            200
                        )
                        """,
                        "string"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.itemTag",
                        "Assets.itemTag(value1, value2, ...)",
                        "Creates an item tag JSON string with the provided values. " + LOADING_VM_NOTE,
                        """
                        local json = Assets.itemTag(
                            "minecraft:diamond",
                            "minecraft:emerald"
                        )
                        """,
                        "string"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.blockTag",
                        "Assets.blockTag(value1, value2, ...)",
                        "Creates a block tag JSON string. " + LOADING_VM_NOTE,
                        """
                        local json = Assets.blockTag(
                            "minecraft:stone",
                            "minecraft:cobblestone"
                        )
                        """,
                        "string"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.entityTypeTag",
                        "Assets.entityTypeTag(value1, value2, ...)",
                        "Creates an entity type tag JSON string. " + LOADING_VM_NOTE,
                        """
                        local json = Assets.entityTypeTag(
                            "minecraft:zombie",
                            "minecraft:skeleton"
                        )
                        """,
                        "string"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.predicateMatchTool",
                        "Assets.predicateMatchTool(itemId)",
                        "Creates a `minecraft:match_tool` predicate JSON string. " + LOADING_VM_NOTE,
                        """
                        local json = Assets.predicateMatchTool("minecraft:diamond_pickaxe")
                        """,
                        "string"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.locationBlockPredicate",
                        "Assets.locationBlockPredicate(blockId)",
                        "Creates a location-check predicate for a block. " + LOADING_VM_NOTE,
                        """
                        local json = Assets.locationBlockPredicate("minecraft:diamond_block")
                        """,
                        "string"
                )
        );

        LuaDocumentationRegistry.register(LuaDocModule.ASSETS,
                new LuaMethodDoc(
                        "Assets.impossibleAdvancement",
                        "Assets.impossibleAdvancement(title, description, iconItem[, rewardFunction])",
                        "Creates a hidden advancement JSON string using the impossible trigger. " + LOADING_VM_NOTE,
                        """
                        local json = Assets.impossibleAdvancement(
                            "Lua Generated",
                            "Only available after loading",
                            "minecraft:command_block",
                            "minecraft:lua_example_ping"
                        )
                        """,
                        "string"
                )
        );
    }

    private AssetsDocBootstrap() {}
}
