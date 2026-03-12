package de.rsev.minecraft.forge.controller.lua.bridge.player;

public interface LuaPlayerHandle {

    String getName();

    int getId();

    double getX();

    double getY();

    double getZ();

    void sendMessage(String message);

    Object getRaw();
}