package de.rsev.minecraft.forge.controller.lua.tools.documentation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LuaDocGenerator {

    public static void generateMarkdown(Path target, LuaDocModule module, List<LuaMethodDoc> methods)
            throws IOException {

        Files.createDirectories(target);

        Path file = target.resolve(module.displayName() + ".md");

        StringBuilder builder = new StringBuilder();
        builder.append("# ").append(module.displayName()).append("\n\n");

        for (LuaMethodDoc method : methods) {

            builder.append("## ").append(method.name()).append("\n\n");
            builder.append("**Signatur:** `").append(method.signature()).append("`\n\n");
            builder.append(method.description()).append("\n\n");

            builder.append("**Beispiel:**\n");
            builder.append("```lua\n");
            builder.append(method.example()).append("\n");
            builder.append("```\n\n");

            builder.append("**Rückgabe:** ").append(method.returns()).append("\n\n");
        }

        Files.writeString(file, builder.toString());
    }
    
    private LuaDocGenerator() {}
}
