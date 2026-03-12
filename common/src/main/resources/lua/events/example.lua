-- lua/events/example.lua
-- Reloadable runtime events

events.on("player_join", function(ctx)
    print("Player joined: " .. ctx.player:name())
end)
