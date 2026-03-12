package de.rsev.minecraft.forge.controller.lua.bridge.timer;

public class LuaTask {

    private final Runnable action;
    private int ticksRemaining;
    private final boolean repeating;
    private final int repeatInterval;

    public LuaTask(Runnable action, int ticks, boolean repeating) {
        this.action = action;
        this.ticksRemaining = ticks;
        this.repeating = repeating;
        this.repeatInterval = ticks;
    }

    public boolean tick() {
        ticksRemaining--;

        if (ticksRemaining <= 0) {
            action.run();

            if (repeating) {
                ticksRemaining = repeatInterval;
                return false;
            }

            return true;
        }

        return false;
    }
}
