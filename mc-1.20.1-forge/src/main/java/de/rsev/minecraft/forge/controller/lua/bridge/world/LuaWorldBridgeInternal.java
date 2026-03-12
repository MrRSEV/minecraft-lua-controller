package de.rsev.minecraft.forge.controller.lua.bridge.world;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import de.rsev.minecraft.forge.controller.lua.LuaRuntime;
import de.rsev.minecraft.forge.controller.lua.bridge.player.*;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import org.luaj.vm2.*;

public class LuaWorldBridgeInternal {

    private static ServerLevel level() {
        MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
        return server == null ? null : server.overworld();
    }

    // =========================
    // BLOCKS
    // =========================
    public static boolean setBlock(int x, int y, int z, String blockId) {
        return LuaWorldBridgeInternal.setBlock(x, y, z, blockId);
    }

    public static String getBlock(int x, int y, int z) {
        ServerLevel level = level();
        if (level == null) return null;

        BlockState state = level.getBlockState(new BlockPos(x, y, z));
        return state.getBlock().toString();
    }

    public static boolean setSignText(int x, int y, int z, String l1, String l2) {

        try {
            var server = LuaRuntime.getServer();
            if (server == null) return false;

            var level = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer().overworld();
            if (level == null) return false;

            var pos = new BlockPos(x, y, z);
            BlockState state = level.getBlockState(pos);
            var be = level.getBlockEntity(pos);

            if (!(be instanceof SignBlockEntity sign)) {
                LuaLogger.warn("setSignText → no sign at {} {} {}", x, y, z);
                return false;
            }

            SignText front = sign.getText(true)
                    .setMessage(0, Component.literal(l1))
                    .setMessage(1, Component.literal(l2));

            SignText back = sign.getText(false)
                    .setMessage(0, Component.literal(l1))
                    .setMessage(1, Component.literal(l2));

            sign.setText(front, true);
            sign.setText(back, false);

            sign.setChanged();

            level.sendBlockUpdated(pos, state, state, 3);
            level.getChunkSource().blockChanged(pos);

            LuaLogger.info("setSignText → {} {} {}", x, y, z);

            return true;

        } catch (Exception e) {
            LuaLogger.error("setSignText failed", e);
            return false;
        }
    }

    // =========================
    // ENTITIES
    // =========================
    public static boolean spawnEntity(String id, double x, double y, double z) {
        return LuaWorldBridgeInternal.spawnEntity(id, x, y, z);
    }

    public static LuaTable getAllEntities() {
        return getEntities(null, Double.NaN, Double.NaN, Double.NaN, 0);
    }

    public static LuaTable getEntities(String type, double x, double y, double z, double range) {

        LuaTable table = new LuaTable();
        ServerLevel level = level();
        if (level == null) return table;

        int i = 1;

        for (Entity e : level.getAllEntities()) {

            if (type != null) {

                var key = net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(e.getType());
                //LuaLogger.info("EntityCheck → {}", key);

                if (key == null || !key.toString().equals(type))
                    continue;
            }

            if (!Double.isNaN(x) && range > 0) {
                double dx = e.getX() - x;
                double dy = e.getY() - y;
                double dz = e.getZ() - z;
                if (dx*dx + dy*dy + dz*dz > range*range)
                    continue;
            }

            LuaTable ent = new LuaTable();
            ent.set("id", e.getId());
            ent.set("type", e.getType().toString());
            ent.set("x", e.getX());
            ent.set("y", e.getY());
            ent.set("z", e.getZ());

            table.set(i++, ent);
        }

        return table;
    }

    public static boolean removeEntity(int id) {
        
        ServerLevel level = level();
        if (level == null) return false;

        Entity e = level.getEntity(id);
        if (e == null) return false;

        e.discard();
        return true;
    }

    public static int killAllEntities() {
        ServerLevel level = level();
        
        if (level == null) return 0;

        int killed = 0;
        for (Entity e : level.getAllEntities()) {
            
            if (!(e instanceof net.minecraft.server.level.ServerPlayer)) {
                e.discard();
                killed++;
            }

        }
        
        return killed;
    }

    public static int killEntities(LuaTable filter) {
        String type = filter.get("type").optjstring(null);
        return getEntities(type, Double.NaN, Double.NaN, Double.NaN, 0).length();
    }

    // =========================
    // PLAYERS
    // =========================
    public static LuaTable getPlayers() {
        
        LuaTable table = new LuaTable();
        MinecraftServer server = net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
        
        if (server == null) return table;

        int i = 1;
        for (var p : server.getPlayerList().getPlayers()) {
            table.set(i++, LuaPlayerBridge.wrap(new LuaPlayerWrapper(new ForgeLuaPlayerHandle(p))));
        }
        
        return table;
    }

    public static LuaPlayerWrapper getPlayerById(int id) {
        var p = LuaPlayerLookup.find(id);
        return p == null ? null : new LuaPlayerWrapper(new ForgeLuaPlayerHandle(p));
    }

    // =========================
    // TIME
    // =========================
    public static long getWorldTime() {
        ServerLevel level = level();
        return level == null ? 0 : level.getDayTime();
    }

    public static boolean setWorldTime(long t) {
        
        ServerLevel level = level();
        if (level == null) return false;
        level.setDayTime(t);
        
        return true;
    }

    // =========================
    // WEATHER
    // =========================
    public static String getWeather() {
        
        ServerLevel level = level();
        if (level == null) return "unknown";
        if (level.isThundering()) return "thunder";
        if (level.isRaining()) return "rain";
        
        return "clear";
    }

    public static boolean setWeather(String type) {
        
        ServerLevel level = level();
        if (level == null) return false;

        switch (type) {
            case "clear" -> level.setWeatherParameters(6000, 0, false, false);
            case "rain" -> level.setWeatherParameters(0, 6000, true, false);
            case "thunder" -> level.setWeatherParameters(0, 6000, true, true);
            default -> { return false; }
        }

        return true;
    }
}
