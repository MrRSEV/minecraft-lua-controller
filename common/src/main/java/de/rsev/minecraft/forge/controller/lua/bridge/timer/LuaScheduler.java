package de.rsev.minecraft.forge.controller.lua.bridge.timer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;

public class LuaScheduler {

    private static final List<LuaTask> TASKS = new LinkedList<>();

    public static void schedule(LuaTask task) {
        TASKS.add(task);
    }

    public static void tick() {
        Iterator<LuaTask> it = TASKS.iterator();

        while (it.hasNext()) {
            LuaTask task = it.next();

            if (task.tick()) {
                it.remove();
            }
        }
    }

    public static void clear() {
        TASKS.clear();
        LuaLogger.info("Lua → Timer tasks cleared");
    }

    public static int size() {
        return TASKS.size();
    }

    private LuaScheduler() {}
}
