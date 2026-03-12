package de.rsev.minecraft.forge.controller.lua.tools.documentation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated Wird durch LuaDocumentationRegistry ersetzt.
 *             Diese Klasse wird entfernt.
 */
@Deprecated(forRemoval = true, since = "1.0")
public class LuaApiScanner {

    @Deprecated(forRemoval = true, since = "1.0")
    public static List<String> scan(Class<?> apiClass) {
        List<String> entries = new ArrayList<>();

        for (Method method : apiClass.getDeclaredMethods()) {
            entries.add(method.getName());
        }

        return entries;
    }

    private LuaApiScanner() {}
}

