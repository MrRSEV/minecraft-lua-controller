package de.rsev.minecraft.forge.controller.lua.bridge.world;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class LuaWorldBridge {

    private static LuaWorldAdapter ADAPTER;

    public static void setAdapter(LuaWorldAdapter adapter) {
        ADAPTER = adapter;
    }

    public static LuaTable create() {

        LuaTable world = new LuaTable();

        // =========================
        // BLOCKS
        // =========================
        LuaTable blocks = new LuaTable();

        blocks.set("set", new VarArgFunction() {
            public Varargs invoke(Varargs args) {
                int x = args.arg(1).checkint();
                int y = args.arg(2).checkint();
                int z = args.arg(3).checkint();
                String blockId = args.arg(4).checkjstring();
                return LuaValue.valueOf(ADAPTER.setBlock(x,y,z,blockId));
            }
        });

        blocks.set("get", new VarArgFunction() {
            public Varargs invoke(Varargs args) {
                int x = args.arg(1).checkint();
                int y = args.arg(2).checkint();
                int z = args.arg(3).checkint();
                String id = ADAPTER.getBlock(x,y,z);
                return id == null ? LuaValue.NIL : LuaValue.valueOf(id);
            }
        });

        blocks.set("setSignText", new VarArgFunction() {
            public Varargs invoke(Varargs args) {

                int x = args.arg(1).checkint();
                int y = args.arg(2).checkint();
                int z = args.arg(3).checkint();

                LuaTable text = args.arg(4).checktable();

                String l1 = text.get("line1").optjstring("");
                String l2 = text.get("line2").optjstring("");

                return LuaValue.valueOf(
                        ADAPTER.setSignText(x,y,z,l1,l2)
                );
            }
        });

        world.set("blocks", blocks);

        // =========================
        // ENTITIES
        // =========================
        LuaTable entities = new LuaTable();

        entities.set("spawn", new VarArgFunction() {
            public Varargs invoke(Varargs args) {

                String id = args.arg(1).checkjstring();
                double x = args.arg(2).checkdouble();
                double y = args.arg(3).checkdouble();
                double z = args.arg(4).checkdouble();

                return LuaValue.valueOf(
                        ADAPTER.spawnEntity(id,x,y,z)
                );
            }
        });

        entities.set("get", new VarArgFunction() {
            public Varargs invoke(Varargs args) {

                if (args.narg() == 0 || args.arg(1).isnil()) {
                    return ADAPTER.getAllEntities();
                }

                LuaTable filter = args.arg(1).checktable();

                String type = filter.get("type").optjstring(null);
                double x = filter.get("x").optdouble(Double.NaN);
                double y = filter.get("y").optdouble(Double.NaN);
                double z = filter.get("z").optdouble(Double.NaN);
                double range = filter.get("range").optdouble(0);

                return ADAPTER.getEntities(type,x,y,z,range);
            }
        });

        entities.set("remove", new OneArgFunction() {
            public LuaValue call(LuaValue id) {
                return LuaValue.valueOf(
                        ADAPTER.removeEntity(id.checkint())
                );
            }
        });

        entities.set("kill", new VarArgFunction() {
            public Varargs invoke(Varargs args) {

                if (args.narg()==0 || args.arg(1).isnil())
                    return LuaValue.valueOf(ADAPTER.killAllEntities());

                LuaTable filter = args.arg(1).checktable();
                return LuaValue.valueOf(
                        ADAPTER.killEntities(filter)
                );
            }
        });

        world.set("entities", entities);

        // =========================
        // PLAYERS
        // =========================
        LuaTable players = new LuaTable();

        players.set("get", new ZeroArgFunction() {
            public LuaValue call() {
                return ADAPTER.getPlayers();
            }
        });

        players.set("getById", new OneArgFunction() {
            public LuaValue call(LuaValue idArg) {

                Object p = ADAPTER.getPlayerById(idArg.checkint());

                return p == null
                        ? LuaValue.NIL
                        : CoerceJavaToLua.coerce(p);
            }
        });

        world.set("players", players);

        // =========================
        // TIME
        // =========================
        LuaTable time = new LuaTable();

        time.set("get", new ZeroArgFunction() {
            public LuaValue call() {
                return LuaValue.valueOf(
                        ADAPTER.getWorldTime()
                );
            }
        });

        time.set("set", new OneArgFunction() {
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(
                        ADAPTER.setWorldTime(arg.checklong())
                );
            }
        });

        world.set("time", time);

        // =========================
        // WEATHER
        // =========================
        LuaTable weather = new LuaTable();

        weather.set("get", new ZeroArgFunction() {
            public LuaValue call() {
                return LuaValue.valueOf(
                        ADAPTER.getWeather()
                );
            }
        });

        weather.set("set", new OneArgFunction() {
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(
                        ADAPTER.setWeather(arg.checkjstring())
                );
            }
        });

        world.set("weather", weather);

        return world;
    }

    private LuaWorldBridge(){}
}