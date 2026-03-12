package de.rsev.minecraft.forge.controller.lua.sandbox;

public class LuaSecurityPolicy {

    private final int maxInstructions;
    private final long maxExecutionTimeMs;

    public LuaSecurityPolicy(int maxInstructions, long maxExecutionTimeMs) {
        this.maxInstructions = maxInstructions;
        this.maxExecutionTimeMs = maxExecutionTimeMs;
    }

    public int getMaxInstructions() {
        return maxInstructions;
    }

    public long getMaxExecutionTimeMs() {
        return maxExecutionTimeMs;
    }
}
