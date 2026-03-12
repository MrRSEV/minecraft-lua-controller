// This is an adapter, 
// to exchange data from different forge repos to the common repos

package de.rsev.minecraft.forge.controller.lua.bridge.commands;

import de.rsev.minecraft.forge.controller.lua.LuaRuntime;
import org.luaj.vm2.*;

public class LuaCommandContextAdapter {

    public static LuaTable adapt(LuaRuntime.CommandContext c) {

        LuaTable ctx = new LuaTable();

        if (c.player() instanceof LuaValue luaPlayer) {
            ctx.set("player", luaPlayer);
        }

        if (c.args() != null) {
            LuaTable argsTable = new LuaTable();

            int i = 1;
            for (String arg : c.args()) {
                argsTable.set(i++, LuaValue.valueOf(arg));
            }

            ctx.set("args", argsTable);
        }

        return ctx;
    }

    private LuaCommandContextAdapter() {}
}