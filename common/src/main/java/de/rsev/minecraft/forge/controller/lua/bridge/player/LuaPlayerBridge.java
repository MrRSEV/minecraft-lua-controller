package de.rsev.minecraft.forge.controller.lua.bridge.player;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public class LuaPlayerBridge {

    private static LuaPlayerAdapter ADAPTER;

    public static void setAdapter(LuaPlayerAdapter adapter) {
        ADAPTER = adapter;
    }

    public static LuaTable create() {
        LuaTable player = new LuaTable();

        player.set("get", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue idArg) {
                int playerId = idArg.checkint();
                LuaPlayerWrapper wrapper = ADAPTER.getPlayer(playerId);

                if (wrapper == null) {
                    return LuaValue.NIL;
                }

                return wrap(wrapper);
            }
        });

        player.set("getAll", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return ADAPTER.getAllPlayers();
            }
        });

        player.set("addItem", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                int playerId = args.arg(1).checkint();
                String itemId = args.arg(2).checkjstring();
                int count = args.arg(3).checkint();

                return LuaValue.valueOf(ADAPTER.addItem(playerId, itemId, count));
            }
        });

        player.set("removeItem", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                int playerId = args.arg(1).checkint();
                String itemId = args.arg(2).checkjstring();
                int count = args.arg(3).checkint();

                return LuaValue.valueOf(ADAPTER.removeItem(playerId, itemId, count));
            }
        });

        player.set("sendMessage", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                int playerId = args.arg(1).checkint();
                String message = args.arg(2).checkjstring();

                return LuaValue.valueOf(ADAPTER.sendMessage(playerId, message));
            }
        });

        return player;
    }

    public static LuaTable wrap(LuaPlayerWrapper wrapper) {
        LuaTable table = new LuaTable();
        table.set("__player_wrapper", LuaValue.userdataOf(wrapper));

        table.set("id", LuaValue.valueOf(wrapper.getId()));
        table.set("name", LuaValue.valueOf(wrapper.getName()));
        table.set("x", LuaValue.valueOf(wrapper.getX()));
        table.set("y", LuaValue.valueOf(wrapper.getY()));
        table.set("z", LuaValue.valueOf(wrapper.getZ()));

        table.set("getName", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return LuaValue.valueOf(resolveWrapper(self, wrapper).getName());
            }
        });

        table.set("getId", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return LuaValue.valueOf(resolveWrapper(self, wrapper).getId());
            }
        });

        table.set("getPosition", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                LuaPlayerWrapper current = resolveWrapper(self, wrapper);
                LuaTable pos = new LuaTable();

                pos.set("x", LuaValue.valueOf(current.getX()));
                pos.set("y", LuaValue.valueOf(current.getY()));
                pos.set("z", LuaValue.valueOf(current.getZ()));

                return pos;
            }
        });

        table.set("sendMessage", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue msg) {
                LuaPlayerWrapper current = resolveWrapper(self, wrapper);
                String text = msg.checkjstring();

                LuaLogger.info("Lua -> sendMessage: {}", text);
                current.sendMessage(text);

                return LuaValue.TRUE;
            }
        });

        table.set("getX", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue self) {
                return LuaValue.valueOf(resolveWrapper(self, wrapper).getX());
            }
        });

        return table;
    }

    private static LuaPlayerWrapper resolveWrapper(LuaValue self, LuaPlayerWrapper fallback) {
        if (self != null && self.istable()) {
            Object userdata = self.get("__player_wrapper").touserdata(LuaPlayerWrapper.class);

            if (userdata instanceof LuaPlayerWrapper playerWrapper) {
                return playerWrapper;
            }
        }

        return fallback;
    }

    private LuaPlayerBridge() {}
}
