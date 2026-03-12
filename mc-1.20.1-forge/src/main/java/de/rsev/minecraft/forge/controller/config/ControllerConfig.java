package de.rsev.minecraft.forge.controller.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ControllerConfig {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLE_LUA;
    public static final ForgeConfigSpec.IntValue LUA_TICK_BUDGET;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("lua");

        ENABLE_LUA = builder
                .comment("Enable Lua runtime")
                .define("enableLua", true);

        LUA_TICK_BUDGET = builder
                .comment("Max Lua executions per tick")
                .defineInRange("tickBudget", 100, 1, 10_000);

        builder.pop();

        SPEC = builder.build();
    }

    private ControllerConfig() {}
}
