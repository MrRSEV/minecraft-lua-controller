-- lua/commands/example.lua
-- Reloadable Lua command

commands.register("luatest", {
    level = 0,
    description = "Example Lua command",
    execute = function(ctx)
        ctx:reply("Lua is working!")
    end
})
