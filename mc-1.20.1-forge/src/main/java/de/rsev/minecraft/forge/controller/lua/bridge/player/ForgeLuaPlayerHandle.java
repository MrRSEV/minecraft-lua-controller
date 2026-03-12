package de.rsev.minecraft.forge.controller.lua.bridge.player;

import java.util.UUID;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ForgeLuaPlayerHandle implements LuaPlayerHandle {

    private final UUID playerUUID;
    private final MinecraftServer server;

    public ForgeLuaPlayerHandle(ServerPlayer player) {
        this.playerUUID = player.getUUID();
        this.server = player.server;
    }

    private ServerPlayer getLivePlayer() {
        if (server == null) return null;
        return server.getPlayerList().getPlayer(playerUUID);
    }

    @Override
    public String getName() {
        ServerPlayer p = getLivePlayer();
        return p != null ? p.getName().getString() : "Unknown";
    }

    @Override
    public int getId() {
        ServerPlayer p = getLivePlayer();
        return p != null ? p.getId() : -1;
    }

    @Override
    public double getX() {
        ServerPlayer p = getLivePlayer();
        return p != null ? p.getX() : 0;
    }

    @Override
    public double getY() {
        ServerPlayer p = getLivePlayer();
        return p != null ? p.getY() : 0;
    }

    @Override
    public double getZ() {
        ServerPlayer p = getLivePlayer();
        return p != null ? p.getZ() : 0;
    }

    @Override
    public void sendMessage(String message) {

        ServerPlayer p = getLivePlayer();

        if (p == null) {
            LuaLogger.info("LivePlayer null → drop");
            return;
        }

        p.server.execute(() -> {

            if (p.connection == null) {
                LuaLogger.info("connection null → drop");
                return;
            }

            p.sendSystemMessage(Component.literal(message));
        });
    }

    @Override
    public Object getRaw() {
        return getLivePlayer();
    }
}