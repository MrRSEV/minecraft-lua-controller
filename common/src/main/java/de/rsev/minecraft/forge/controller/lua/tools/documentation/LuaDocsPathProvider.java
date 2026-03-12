// This is an providing adapter, 
// to exchange data from different forge repos to the common repos
package de.rsev.minecraft.forge.controller.lua.tools.documentation;

import java.nio.file.Path;

public interface LuaDocsPathProvider {

    Path getDocsDirectory();

}