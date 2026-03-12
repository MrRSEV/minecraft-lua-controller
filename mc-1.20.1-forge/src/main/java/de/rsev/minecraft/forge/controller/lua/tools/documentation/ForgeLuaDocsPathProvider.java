package de.rsev.minecraft.forge.controller.lua.tools.documentation;

import java.nio.file.Path;

import de.rsev.minecraft.forge.controller.RSEVControllerMod;
import net.minecraftforge.fml.loading.FMLPaths;

public class ForgeLuaDocsPathProvider implements LuaDocsPathProvider {

    @Override
    public Path getDocsDirectory() {

        return FMLPaths.MODSDIR.get()
                .resolve(RSEVControllerMod.getDataDirectoryName())
                .resolve("lua")
                .resolve("docs");
    }
}
