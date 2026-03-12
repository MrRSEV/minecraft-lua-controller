package de.rsev.minecraft.forge.controller.lua.bridge.events;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

public class LuaEventsBridge {

    public static LuaTable create() {

        LuaTable events = new LuaTable();

        events.set("on", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue eventArg, LuaValue funcArg) {

                String event = eventArg.checkjstring();

                if (!funcArg.isfunction()) {
                    return LuaValue.error("Events.on expects function");
                }

                LuaEventsBridgeInternal.register(event, funcArg);

                return LuaValue.NIL;
            }
        });

        return events;
    }
}
