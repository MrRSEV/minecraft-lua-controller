package de.rsev.minecraft.forge.controller.lua.bridge.system;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import de.rsev.minecraft.forge.controller.lua.tools.documentation.LuaDocumentationBuilder;

public class LuaSystemAPI {

    public void print(String message) {
        LuaLogger.forgeInfo("[Lua] {}", message);
    }

    public long time() {
        return System.currentTimeMillis();
    }

    public void generateDocumentation() {
        LuaDocumentationBuilder.generate();
    }
}
