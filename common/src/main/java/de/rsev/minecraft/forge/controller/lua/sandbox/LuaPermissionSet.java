package de.rsev.minecraft.forge.controller.lua.sandbox;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LuaPermissionSet {

    private final Set<String> permissions = new HashSet<>();

    public void grant(String permission) {
        permissions.add(permission);
    }

    public boolean has(String permission) {
        return permissions.contains(permission);
    }

    public Set<String> asSet() {
        return Collections.unmodifiableSet(permissions);
    }
}
