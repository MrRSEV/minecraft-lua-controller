package de.rsev.minecraft.forge.controller.paper.lua.bridge.scoreboard;

import de.rsev.minecraft.forge.controller.lua.bridge.scoreboard.LuaScoreboardAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class PaperLuaScoreboardAdapter implements LuaScoreboardAdapter {

    @Override
    public boolean setScore(String objective, int playerId, int value) {
        Player player = getPlayer(playerId);
        if (player == null) {
            return false;
        }

        Objective obj = getOrCreateObjective(objective);
        obj.getScore(player.getName()).setScore(value);
        return true;
    }

    @Override
    public boolean addScore(String objective, int playerId, int value) {
        Player player = getPlayer(playerId);
        if (player == null) {
            return false;
        }

        Objective obj = getOrCreateObjective(objective);
        int current = obj.getScore(player.getName()).getScore();
        obj.getScore(player.getName()).setScore(current + value);
        return true;
    }

    @Override
    public int getScore(String objective, int playerId) {
        Player player = getPlayer(playerId);
        if (player == null) {
            return 0;
        }

        Objective obj = Bukkit.getScoreboardManager().getMainScoreboard().getObjective(objective);
        return obj != null ? obj.getScore(player.getName()).getScore() : 0;
    }

    @Override
    public LuaValue listObjectives() {
        LuaTable table = new LuaTable();
        int index = 1;
        for (Objective objective : Bukkit.getScoreboardManager().getMainScoreboard().getObjectives()) {
            table.set(index++, LuaValue.valueOf(objective.getName()));
        }
        return table;
    }

    private Objective getOrCreateObjective(String objectiveName) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.getObjective(objectiveName);
        if (objective == null) {
            objective = scoreboard.registerNewObjective(objectiveName, Criteria.DUMMY, objectiveName);
        }
        return objective;
    }

    private Player getPlayer(int playerId) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getEntityId() == playerId) {
                return player;
            }
        }
        return null;
    }
}
