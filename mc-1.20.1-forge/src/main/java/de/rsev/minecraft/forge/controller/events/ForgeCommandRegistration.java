package de.rsev.minecraft.forge.controller.events;

import de.rsev.minecraft.forge.controller.lua.bridge.commands.DynamicLuaCommandExecutor;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ForgeCommandRegistration {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        DynamicLuaCommandExecutor.register(event.getDispatcher());
    }
}
