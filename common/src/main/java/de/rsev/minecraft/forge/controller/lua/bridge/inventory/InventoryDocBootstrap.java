package de.rsev.minecraft.forge.controller.lua.bridge.inventory;

import de.rsev.minecraft.forge.controller.lua.tools.documentation.*;

public class InventoryDocBootstrap {

    private static boolean INITIALIZED = false;

    public static void init() {

        if (INITIALIZED) return;
        INITIALIZED = true;

        // =========================
        // Inventory.addItem
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.INVENTORY, new LuaMethodDoc(
                        "Inventory.addItem",
                        "Inventory.addItem(playerId, itemId, count)",
                        "Fügt einem Spieler ein Item hinzu.",
                        """
                        Inventory.addItem(1, "minecraft:diamond", 3)
                        """,
                        "boolean"
                )
        );

        // =========================
        // Inventory.removeItem
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.INVENTORY, new LuaMethodDoc(
                        "Inventory.removeItem",
                        "Inventory.removeItem(playerId, itemId, count)",
                        "Entfernt Items aus dem Inventar eines Spielers.",
                        """
                        Inventory.removeItem(1, "minecraft:stone", 10)
                        """,
                        "boolean"
                )
        );

        // =========================
        // Inventory.get
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.INVENTORY, new LuaMethodDoc(
                        "Inventory.get",
                        "Inventory.get(playerId)",
                        "Gibt einen InventoryWrapper für den Spieler zurück.",
                        """
                        local inv = Inventory.get(1)
                        inv:addItem("minecraft:apple", 5)
                        """,
                        "LuaInventory"
                )
        );

        // =========================
        // LuaInventory:addItem
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.INVENTORY, new LuaMethodDoc(
                        "LuaInventory:addItem",
                        "inventory:addItem(itemId, count)",
                        "Fügt dem Inventar direkt ein Item hinzu.",
                        """
                        local inv = Inventory.get(1)
                        inv:addItem("minecraft:gold_ingot", 2)
                        """,
                        "boolean"
                )
        );

        // =========================
        // LuaInventory:removeItem
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.INVENTORY, new LuaMethodDoc(
                        "LuaInventory:removeItem",
                        "inventory:removeItem(itemId, count)",
                        "Entfernt ein Item direkt aus dem Inventar.",
                        """
                        local inv = Inventory.get(1)
                        inv:removeItem("minecraft:dirt", 64)
                        """,
                        "boolean"
                )
        );

        // =========================
        // LuaInventory:getItemCount
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.INVENTORY, new LuaMethodDoc(
                        "LuaInventory:getItemCount",
                        "inventory:getItemCount(itemId)",
                        "Gibt die Anzahl eines Items im Inventar zurück.",
                        """
                        local inv = Inventory.get(1)
                        local count = inv:getItemCount("minecraft:diamond")
                        System.print(count)
                        """,
                        "number"
                )
        );

        // =========================
        // LuaInventory:clear
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.INVENTORY, new LuaMethodDoc(
                        "LuaInventory:clear",
                        "inventory:clear()",
                        "Leert das komplette Inventar.",
                        """
                        local inv = Inventory.get(1)
                        inv:clear()
                        """,
                        "nil"
                )
        );
    }

    private InventoryDocBootstrap() {}
}