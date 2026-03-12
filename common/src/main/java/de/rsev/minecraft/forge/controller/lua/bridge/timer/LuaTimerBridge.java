package de.rsev.minecraft.forge.controller.lua.bridge.timer;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

public class LuaTimerBridge {

    public static LuaTable create() {

        LuaTable timer = new LuaTable();

        // =========================
        // every(ticks, func)
        // =========================
        timer.set("every", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {

                int ticks = args.arg(1).checkint();
                LuaValue func = args.arg(2);

                if (!func.isfunction()) {
                    return LuaValue.error("Timer.every expects function");
                }

                int id = LuaTimerBridgeInternal.every(ticks, func);

                return LuaValue.valueOf(id);
            }
        });

        // =========================
        // after(ticks, func)
        // =========================
        timer.set("after", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {

                int ticks = args.arg(1).checkint();
                LuaValue func = args.arg(2);

                if (!func.isfunction()) {
                    return LuaValue.error("Timer.after expects function");
                }

                int id = LuaTimerBridgeInternal.after(ticks, func);

                return LuaValue.valueOf(id);
            }
        });

        // =========================
        // cancel(id)
        // =========================
        timer.set("cancel", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue idArg) {

                boolean success = LuaTimerBridgeInternal.cancel(idArg.checkint());

                return LuaValue.valueOf(success);
            }
        });

        return timer;
    }
}
