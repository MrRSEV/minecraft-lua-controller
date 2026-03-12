package de.rsev.minecraft.forge.controller.lua.tools.documentation;

public enum LuaDocModule {

    WORLD("World"),
    TIMER("Timer"),
    PLAYER("Player"),
    SYSTEM("System"),
    INVENTORY("Inventory"),
    COMMANDS("Commands"),
    EVENTS("Events"),
    CONFIG("Config"),
    IO("IO"),
    SCOREBOARD("Scoreboard"),
    ASSETS("Assets");

    private final String displayName;

    LuaDocModule(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
