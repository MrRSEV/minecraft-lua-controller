package de.rsev.minecraft.forge.controller.lua.bridge.player;

import de.rsev.minecraft.forge.controller.lua.tools.documentation.*;

public class PlayerDocBootstrap {

    private static boolean INITIALIZED = false;

    public static void init() {

        if (INITIALIZED) return;
        INITIALIZED = true;

        // =========================
        // Player.get
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.PLAYER,
                new LuaMethodDoc(
                        "Player.get",
                        "Player.get(playerId)",
                        "Gibt einen Spieler-Wrapper zurück.",
                        """
                        local player = Player.get(1)

                        if player then
                            System.print(player:getName())
                        end
                        """,
                        "LuaPlayerWrapper | nil"
                )
        );

        // =========================
        // Player.getAll
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.PLAYER,
                new LuaMethodDoc(
                        "Player.getAll",
                        "Player.getAll()",
                        "Gibt alle Online-Spieler zurück.",
                        """
                        for i, player in ipairs(Player.getAll()) do
                            System.print(player:getName())
                        end
                        """,
                        "table<LuaPlayerWrapper>"
                )
        );

        // =========================
        // Player.addItem
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.PLAYER,
                new LuaMethodDoc(
                        "Player.addItem",
                        "Player.addItem(playerId, itemId, count)",
                        "Fügt einem Spieler ein Item hinzu.",
                        """
                        Player.addItem(1, "minecraft:diamond", 3)
                        """,
                        "boolean"
                )
        );

        // =========================
        // Player.removeItem
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.PLAYER,
                new LuaMethodDoc(
                        "Player.removeItem",
                        "Player.removeItem(playerId, itemId, count)",
                        "Entfernt Items aus dem Inventar eines Spielers.",
                        """
                        Player.removeItem(1, "minecraft:dirt", 10)
                        """,
                        "boolean"
                )
        );

        // =========================
        // Player.sendMessage
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.PLAYER,
                new LuaMethodDoc(
                        "Player:sendMessage",
                        "Player:sendMessage(message)",
                        "Sendet eine Chatnachricht an einen Spieler.",
                        """
                        Player:sendMessage("Hallo aus Lua!")
                        """,
                        "boolean"
                )
        );

        // =========================
        // Wrapper.getName
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.PLAYER,
                new LuaMethodDoc(
                        "player:getName",
                        "player:getName()",
                        "Gibt den Spielernamen zurück.",
                        """
                        local player = Player.get(1)

                        if player then
                            System.print(player:getName())
                        end
                        """,
                        "string"
                )
        );

        // =========================
        // Wrapper.getId
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.PLAYER,
                new LuaMethodDoc(
                        "player:getId",
                        "player:getId()",
                        "Gibt die Entity-ID des Spielers zurück.",
                        """
                        local player = Player.get(1)

                        if player then
                            System.print(player:getId())
                        end
                        """,
                        "number"
                )
        );

        // =========================
        // Wrapper.getPosition
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.PLAYER,
                new LuaMethodDoc(
                        "player:getPosition",
                        "player:getPosition()",
                        "Gibt die aktuelle Position zurück.",
                        """
                        local player = Player.get(1)

                        if player then
                            local pos = player:getPosition()
                            System.print(pos.x .. ", " .. pos.y .. ", " .. pos.z)
                        end
                        """,
                        "table { x, y, z }"
                )
        );

        // =========================
        // Wrapper.sendMessage
        // =========================
        LuaDocumentationRegistry.register(LuaDocModule.PLAYER,
                new LuaMethodDoc(
                        "player.sendMessage",
                        "player.sendMessage(playerPbj, message)",
                        "Sendet eine Nachricht direkt an diesen Spieler.",
                        """
                        local player = Player.get(1)

                        if player then
                            player:sendMessage("Hallo direkt!")
                        end

                        oder besser und sicherer:

                        -- =========================
                        -- Helper: sichere Chat-Ausgabe
                        -- =========================
                        local function sendPlayerMessage(player, msg)

                                if not player then
                                        System.print("❌ sendPlayerMessage → player nil")
                                        return
                                end

                                if msg == nil then
                                        System.print("❌ sendPlayerMessage → msg nil")
                                        return
                                end

                                local msgStr = tostring(msg)

                                System.print("DEBUG → msg      = " .. msgStr)
                                System.print("DEBUG → msg type = " .. type(msg))

                                if player.sendMessage then

                                        local ok, err = pcall(function()
                                                -- IMMER msgStr verwenden!
                                                player.sendMessage(msgStr)
                                        end)

                                        if not ok then
                                                System.print("❌ player.sendMessage crashed → " .. tostring(err))
                                        else
                                                System.print("✅ Chat gesendet")
                                        end

                                else
                                        System.print("❌ player.sendMessage missing")
                                end
                        end

                        """,
                        "boolean"
                )
        );
    }

    private PlayerDocBootstrap() {}
}