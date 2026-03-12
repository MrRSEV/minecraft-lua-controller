package de.rsev.minecraft.forge.controller.lua.tools.documentation;

import java.util.*;

public class LuaDocumentationRegistry {

    private static final Map<LuaDocModule, List<LuaMethodDoc>> REGISTRY = new HashMap<>();

    public static void register(LuaDocModule module, LuaMethodDoc doc) {
        REGISTRY.computeIfAbsent(module, k -> new ArrayList<>()).add(doc);
    }

    public static List<LuaMethodDoc> get(LuaDocModule module) {
        return REGISTRY.getOrDefault(module, List.of());
    }

    public static Map<LuaDocModule, List<LuaMethodDoc>> getAll() {
        return Map.copyOf(REGISTRY);
    }

    private LuaDocumentationRegistry() {}
}