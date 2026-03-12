package de.rsev.minecraft.forge.controller.lua.bridge.inventory;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class LuaInventoryBridge {

    private static LuaInventoryAdapter ADAPTER;

    public static void setAdapter(LuaInventoryAdapter adapter) {
        ADAPTER = adapter;
    }

    public static LuaTable create() {

        LuaTable inventory = new LuaTable();

        // =========================
        // addItem(playerId, itemId, count)
        // =========================
        inventory.set("addItem", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {

                int playerId = args.arg(1).checkint();
                String itemId = args.arg(2).checkjstring();
                int count = args.arg(3).checkint();

                boolean success = ADAPTER.addItem(playerId, itemId, count);

                return LuaValue.valueOf(success);
            }
        });

        // =========================
        // removeItem(playerId, itemId, count)
        // =========================
        inventory.set("removeItem", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {

                int playerId = args.arg(1).checkint();
                String itemId = args.arg(2).checkjstring();
                int count = args.arg(3).checkint();

                boolean success = ADAPTER.removeItem(playerId, itemId, count);

                return LuaValue.valueOf(success);
            }
        });

        // =========================
        // get(playerId)
        // =========================
        inventory.set("get", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue idArg) {

                int playerId = idArg.checkint();

                Object wrapper = ADAPTER.getInventory(playerId);

                if (wrapper == null) {
                    return LuaValue.NIL;
                }

                return CoerceJavaToLua.coerce(wrapper);
            }
        });

        // =========================
        // addItemToBlock(x, y, z, itemId, count)
        // =========================
        inventory.set("addItemToBlock", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {

                int x = args.arg(1).checkint();
                int y = args.arg(2).checkint();
                int z = args.arg(3).checkint();

                String itemId = args.arg(4).checkjstring();
                int count = args.arg(5).checkint();

                boolean success = ADAPTER.addItemToBlock(x, y, z, itemId, count);

                return LuaValue.valueOf(success);
            }
        });

        return inventory;
    }
}