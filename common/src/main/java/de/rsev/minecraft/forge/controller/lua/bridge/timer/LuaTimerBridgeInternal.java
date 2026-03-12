package de.rsev.minecraft.forge.controller.lua.bridge.timer;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import org.luaj.vm2.LuaValue;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LuaTimerBridgeInternal {

    private static final AtomicInteger ID_GEN = new AtomicInteger();
    private static final Map<Integer, TimerTask> TASKS = new HashMap<>();

    public static int every(int ticks, LuaValue func) {

        int id = ID_GEN.incrementAndGet();

        TASKS.put(id, new TimerTask(ticks, func, true));

        LuaLogger.info("Lua → Timer.every id={} ticks={}", id, ticks);

        return id;
    }

    public static int after(int ticks, LuaValue func) {

        int id = ID_GEN.incrementAndGet();

        TASKS.put(id, new TimerTask(ticks, func, false));

        LuaLogger.info("Lua → Timer.after id={} ticks={}", id, ticks);

        return id;
    }

    public static boolean cancel(int id) {

        LuaLogger.info("Lua → Timer.cancel id={}", id);

        return TASKS.remove(id) != null;
    }

    public static void clear() {

        int size = TASKS.size();
        TASKS.clear();
        ID_GEN.set(0);   // ✅ IDs zurücksetzen

        LuaLogger.info("Lua → Timer tasks cleared ({} removed)", size);
    }

    public static int size() {
        return TASKS.size();
    }



    // Wird von Engine Tick aufgerufen
    public static void tick() {

        Iterator<Map.Entry<Integer, TimerTask>> it = TASKS.entrySet().iterator();

        while (it.hasNext()) {

            var entry = it.next();
            TimerTask task = entry.getValue();

            task.counter++;

            if (task.counter >= task.ticks) {

                try {
                    task.func.call();
                } catch (Exception e) {
                    LuaLogger.error("Lua → Timer execution failed", e);
                }

                if (task.repeat) {
                    task.counter = 0;
                } else {
                    it.remove();
                }
            }
        }
    }

    private static class TimerTask {
        int ticks;
        int counter = 0;
        LuaValue func;
        boolean repeat;

        TimerTask(int ticks, LuaValue func, boolean repeat) {
            this.ticks = ticks;
            this.func = func;
            this.repeat = repeat;
        }
    }
}
