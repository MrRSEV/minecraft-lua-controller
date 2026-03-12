package de.rsev.minecraft.forge.controller.lua.bridge.scoreboard;

import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerLookup;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.*;

import org.luaj.vm2.*;

public class LuaScoreboardBridgeInternal {

    private static MinecraftServer server() {
        return net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
    }

    private static ServerPlayer getPlayer(int playerId) {
        return LuaPlayerLookup.find(playerId);
    }

    private static Objective getObjective(String name) {

        MinecraftServer server = server();
        if (server == null) return null;

        Scoreboard scoreboard = server.getScoreboard();
        return scoreboard.getObjective(name);
    }

    public static boolean setScore(String objectiveName, int playerId, int value) {

        ServerPlayer player = getPlayer(playerId);
        Objective obj = getObjective(objectiveName);

        if (player == null || obj == null) return false;

        Scoreboard scoreboard = server().getScoreboard();
        Score score = scoreboard.getOrCreatePlayerScore(player.getScoreboardName(), obj);

        score.setScore(value);

        return true;
    }

    public static boolean addScore(String objectiveName, int playerId, int value) {

        ServerPlayer player = getPlayer(playerId);
        Objective obj = getObjective(objectiveName);

        if (player == null || obj == null) return false;

        Scoreboard scoreboard = server().getScoreboard();
        Score score = scoreboard.getOrCreatePlayerScore(player.getScoreboardName(), obj);

        score.setScore(score.getScore() + value);

        return true;
    }

    public static int getScore(String objectiveName, int playerId) {

        ServerPlayer player = getPlayer(playerId);
        Objective obj = getObjective(objectiveName);

        if (player == null || obj == null) return 0;

        Scoreboard scoreboard = server().getScoreboard();

        if (!scoreboard.hasPlayerScore(player.getScoreboardName(), obj)) {
            return 0;
        }

        return scoreboard.getOrCreatePlayerScore(player.getScoreboardName(), obj).getScore();
    }

    public static LuaTable listObjectives() {

        LuaTable table = new LuaTable();

        MinecraftServer server = server();
        if (server == null) return table;

        int i = 1;

        for (Objective obj : server.getScoreboard().getObjectives()) {
            table.set(i++, LuaValue.valueOf(obj.getName()));
        }

        return table;
    }
}