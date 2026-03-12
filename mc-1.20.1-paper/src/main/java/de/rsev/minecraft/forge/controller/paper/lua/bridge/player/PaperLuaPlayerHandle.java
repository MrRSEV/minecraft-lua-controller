package de.rsev.minecraft.forge.controller.paper.lua.bridge.player;

import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerHandle;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PaperLuaPlayerHandle implements LuaPlayerHandle {

    private final UUID uuid;

    public PaperLuaPlayerHandle(Player player) {
        this.uuid = player.getUniqueId();
    }

    private Player live() {
        return org.bukkit.Bukkit.getPlayer(uuid);
    }

    @Override
    public String getName() {
        Player player = live();
        return player != null ? player.getName() : "Unknown";
    }

    @Override
    public int getId() {
        Player player = live();
        return player != null ? player.getEntityId() : -1;
    }

    @Override
    public double getX() {
        Player player = live();
        return player != null ? player.getLocation().getX() : 0;
    }

    @Override
    public double getY() {
        Player player = live();
        return player != null ? player.getLocation().getY() : 0;
    }

    @Override
    public double getZ() {
        Player player = live();
        return player != null ? player.getLocation().getZ() : 0;
    }

    @Override
    public void sendMessage(String message) {
        Player player = live();
        if (player != null) {
            player.sendMessage(message);
        }
    }

    @Override
    public Object getRaw() {
        return live();
    }
}
