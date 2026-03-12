package de.rsev.minecraft.forge.controller.lua.tools.documentation;

import java.nio.file.Path;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;

public class LuaDocumentationBuilder {

    private static boolean INITIALIZED = false;

    private static LuaDocsPathProvider PATH_PROVIDER;

    public static void setPathProvider(LuaDocsPathProvider provider) {
        PATH_PROVIDER = provider;
    }

    public static void init() {
        if (INITIALIZED) return;
        INITIALIZED = true;
    }

    public static void generate() {

        Path docsDir = PATH_PROVIDER.getDocsDirectory();

        try {

            LuaDocumentationBootstrap.init();

            for (LuaDocModule module : LuaDocModule.values()) {

                LuaDocGenerator.generateMarkdown(
                        docsDir,
                        module,
                        LuaDocumentationRegistry.get(module)
                );
            }

            LuaIndexGenerator.generate(docsDir);

            LuaLogger.forgeInfo("Lua documentation generated");

        } catch (Exception e) {
            LuaLogger.forgeError("Failed to generate Lua documentation", e);
        }
    }

    private LuaDocumentationBuilder() {}
}