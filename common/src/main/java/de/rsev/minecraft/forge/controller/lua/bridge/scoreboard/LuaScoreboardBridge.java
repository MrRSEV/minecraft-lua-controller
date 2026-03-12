package de.rsev.minecraft.forge.controller.lua.bridge.scoreboard;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

public class LuaScoreboardBridge {

    private static LuaScoreboardAdapter ADAPTER;

    public static void setAdapter(LuaScoreboardAdapter adapter) {
        ADAPTER = adapter;
    }

    public static LuaTable create() {

        LuaTable scoreboard = new LuaTable();

        // Scores
        scoreboard.set("set", new SetScoreFunction());
        scoreboard.set("add", new AddScoreFunction());
        scoreboard.set("get", new GetScoreFunction());

        // Objectives
        scoreboard.set("listObjectives", new ListObjectivesFunction());

        return scoreboard;
    }

    // =========================
    // Scoreboard.set(objective, playerId, value)
    // =========================
    private static class SetScoreFunction extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {

            try {
                String objective = args.arg(1).checkjstring();
                int playerId = args.arg(2).checkint();
                int value = args.arg(3).checkint();

                boolean success = ADAPTER.setScore(objective, playerId, value);
                return LuaValue.valueOf(success);

            } catch (LuaError e) {
                return LuaValue.error("Scoreboard.set(objective, playerId, value)");
            }
        }
    }

    // =========================
    // Scoreboard.add(objective, playerId, value)
    // =========================
    private static class AddScoreFunction extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {

            try {
                String objective = args.arg(1).checkjstring();
                int playerId = args.arg(2).checkint();
                int value = args.arg(3).checkint();

                boolean success = ADAPTER.addScore(objective, playerId, value);
                return LuaValue.valueOf(success);

            } catch (LuaError e) {
                return LuaValue.error("Scoreboard.add(objective, playerId, value)");
            }
        }
    }

    // =========================
    // Scoreboard.get(objective, playerId)
    // =========================
    private static class GetScoreFunction extends VarArgFunction {
        @Override
        public Varargs invoke(Varargs args) {

            try {
                String objective = args.arg(1).checkjstring();
                int playerId = args.arg(2).checkint();

                int score = ADAPTER.getScore(objective, playerId);
                return LuaValue.valueOf(score);

            } catch (LuaError e) {
                return LuaValue.error("Scoreboard.get(objective, playerId)");
            }
        }
    }

    // =========================
    // Scoreboard.listObjectives()
    // =========================
    private static class ListObjectivesFunction extends ZeroArgFunction {
        @Override
        public LuaValue call() {
            return ADAPTER.listObjectives();
        }
    }

    private LuaScoreboardBridge() {}
}