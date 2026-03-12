package de.rsev.minecraft.forge.controller.lua.bridge.config;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.*;

public class LuaConfigBridge {

    public static LuaTable create() {

        LuaTable config = new LuaTable();

        config.set("load", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue pathArg) {
                return LuaConfigBridgeInternal.load(pathArg.checkjstring());
            }
        });

        config.set("save", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue pathArg, LuaValue tableArg) {

                if (!tableArg.istable()) {
                    return LuaValue.error("Config.save expects table");
                }

                boolean success = LuaConfigBridgeInternal.save(
                        pathArg.checkjstring(),
                        (LuaTable) tableArg
                );

                return LuaValue.valueOf(success);
            }
        });

        return config;
    }
}
