-- =========================================================
-- CONFIG / DEBUG
-- =========================================================

local is_debug = false

local function DEBUG(msg)
    if is_debug then
        System.print("[DEBUG] " .. tostring(msg))
    end
end

-- =========================================================
-- Chat Helper (LuaJ SAFE)
-- =========================================================

local function sendPlayerMessage(player, msg)

    if not player then
        System.print("❌ sendPlayerMessage → player nil")
        return
    end

    if msg == nil then
        System.print("❌ sendPlayerMessage → msg nil")
        return
    end

    local msgStr = tostring(msg)

    if player.sendMessage then
        local ok, err = pcall(function()
            player.sendMessage(msgStr)
        end)

        if not ok then
            System.print("❌ sendMessage crashed → " .. tostring(err))
        end
    else
        System.print("❌ player.sendMessage missing")
    end
end

-- =========================================================
-- Header / Config
-- =========================================================

local rewardName = "dorfbewohner_zaehlen"
local sep = "/"

local baseDir = SCRIPT_DIR or WORLD_DIR
local configFile = baseDir .. sep .. rewardName .. ".toml"

-- =========================================================
-- Default Config
-- =========================================================

local defaultConfig = {
    marker = { id = "main", x = 0, y = 64, z = 0 },

    reward = {
        items = {
            { id = "minecraft:diamond", count = 5 }
        }
    },

    settings = {
        villagerRange = 20,
        ticksRequired = 10
    }
}

-- =========================================================
-- File Helpers
-- =========================================================

local function fileExists(path)
    return File and File.exists and File.exists(path)
end

local function writeDefaultConfig()

    if not File or not File.write then
        System.print("❌ File.write missing")
        return false
    end

    System.print("📄 Schreibe Default Config → " .. tostring(configFile))

    return File.write(configFile, [=[

[marker]
id = "main"
x = 0
y = 64
z = 0

[settings]
villagerRange = 20
ticksRequired = 10

[[reward.items]]
id = "minecraft:diamond"
count = 5

]=])
end

-- =========================================================
-- Config Loading
-- =========================================================

local function loadConfig()

    if not fileExists(configFile) then
        System.print("⚠ Keine Config gefunden → erstelle Default")

        if not writeDefaultConfig() then
            return defaultConfig
        end
    end

    if not Toml or not Toml.parse then
        System.print("❌ Toml.parse missing → fallback")
        return defaultConfig
    end

    System.print("📖 Lade Config → " .. tostring(configFile))

    local cfg = Toml.parse(configFile)
    return cfg or defaultConfig
end

CONFIG = loadConfig()

-- =========================================================
-- CONFIG Normalisierung
-- =========================================================

if type(CONFIG) ~= "table" then
    CONFIG = defaultConfig
end

CONFIG.marker = CONFIG.marker or defaultConfig.marker
CONFIG.reward = CONFIG.reward or defaultConfig.reward
CONFIG.settings = CONFIG.settings or defaultConfig.settings
CONFIG.reward.items = CONFIG.reward.items or defaultConfig.reward.items

CONFIG.marker.x = tonumber(CONFIG.marker.x) or 0
CONFIG.marker.y = tonumber(CONFIG.marker.y) or 64
CONFIG.marker.z = tonumber(CONFIG.marker.z) or 0

DEBUG("Marker → "
    .. CONFIG.marker.x .. " "
    .. CONFIG.marker.y .. " "
    .. CONFIG.marker.z)

-- =========================================================
-- Runtime Player State
-- =========================================================

local playerState = {}

local function getPlayerData(id)
    if not playerState[id] then
        playerState[id] = {
            timer = 0,
            active = false
        }
    end
    return playerState[id]
end

-- =========================================================
-- Villager Counting
-- =========================================================

local function countVillagers(player, range)

    if not World or not World.entities or not World.entities.get then
        return 0
    end

    if not player or not player.getPosition then
        return 0
    end

    local pos = player:getPosition()
    if not pos then return 0 end

    local villagers = World.entities.get({
        type  = "minecraft:villager",
        x     = pos.x,
        y     = pos.y,
        z     = pos.z,
        range = range
    })

    return villagers and #villagers or 0
end

-- =========================================================
-- Reward Spawn (STABIL)
-- =========================================================

local function spawnReward(player)

    if not player then return end

    local okName, name = pcall(function()
        return player:getName()
    end)

    if not okName or not name then
        System.print("❌ spawnReward → getName failed")
        return
    end

    local marker = CONFIG.marker
    local settings = CONFIG.settings
    local reward = CONFIG.reward

    local x = tonumber(marker.x)
    local y = tonumber(marker.y)
    local z = tonumber(marker.z)

    if not x or not y or not z then
        System.print("❌ Marker coords invalid")
        return
    end

    local villagers = countVillagers(player, settings.villagerRange)

    System.print("🎁 Reward für " .. name)

    -- Blocks setzen
    World.blocks.set(x, y, z, "minecraft:chest")
    World.blocks.set(x, y + 1, z, "minecraft:oak_sign")

    -- Delay für BlockEntities
    Timer.after(1, function()

        -- Chest Items
        if Inventory and Inventory.addItemToBlock then

            for i = 1, #reward.items do
                local item = reward.items[i]

                if item and item.id and item.count then
                    Inventory.addItemToBlock(x, y, z, item.id, item.count)
                else
                    System.print("⚠ Invalid reward item @" .. tostring(i))
                end
            end

        else
            System.print("⚠ Inventory.addItemToBlock missing")
        end

        -- Sign Text
        if World.blocks and World.blocks.setSignText then

            World.blocks.setSignText(x, y + 1, z, {
                line1 = "Spieler: " .. name,
                line2 = "Dorfbewohner: " .. villagers
            })

        else
            System.print("⚠ setSignText missing")
        end

    end)
end

-- =========================================================
-- Timer Loop
-- =========================================================

Timer.every(1, function()

    local ticksRequired = tonumber(CONFIG.settings.ticksRequired) or 10

    local okPlayers, players = pcall(World.getPlayers)
    if not okPlayers or type(players) ~= "table" then return end

    for i = 1, 128 do

        local player = players[i]
        if not player then break end

        local okId, playerId = pcall(function()
            return player:getId()
        end)

        if okId and playerId then

            local data = getPlayerData(playerId)

            DEBUG("👤 Player " .. playerId
                .. " active=" .. tostring(data.active)
                .. " timer=" .. tostring(data.timer))

            if data.active then

                data.timer = data.timer + 1

                if data.timer >= ticksRequired then
                    spawnReward(player)
                    data.timer = 0
                    data.active = false
                end
            end
        end
    end
end)

-- =========================================================
-- Commands
-- =========================================================

Commands.register(rewardName, function(ctx)

    if not ctx or not ctx.player then
        System.print("❌ Command → ctx/player missing")
        return
    end

    local player = ctx.player
    local sub = ctx.args and ctx.args[1] or "start"

    if sub == "start" then

        local ok, playerId = pcall(function()
            return player:getId()
        end)

        if not ok or not playerId then return end

        local data = getPlayerData(playerId)
        data.timer = 0
        data.active = true

        sendPlayerMessage(player, "§a▶ Timer gestartet")
        System.print("▶ Timer gestartet")

        return
    end

    if sub == "stop" then

        local ok, playerId = pcall(function()
            return player:getId()
        end)

        if not ok or not playerId then return end

        local data = getPlayerData(playerId)
        data.active = false

        sendPlayerMessage(player, "§c⏸ Timer gestoppt")
        System.print("⏸ Timer gestoppt")

        return
    end

    if sub == "setmarker" then

        local okPos, pos = pcall(function()
            return player:getPosition()
        end)

        if not okPos or not pos then
            System.print("❌ getPosition failed")
            return
        end

        CONFIG.marker.x = math.floor(pos.x)
        CONFIG.marker.y = math.floor(pos.y)
        CONFIG.marker.z = math.floor(pos.z)

        if Toml and Toml.write then
            Toml.write(configFile, CONFIG)
        end

        sendPlayerMessage(player,
            "§b📍 Marker gesetzt bei §7"
            .. CONFIG.marker.x .. " "
            .. CONFIG.marker.y .. " "
            .. CONFIG.marker.z
        )

        System.print("📍 Marker gesetzt")

        return
    end

    sendPlayerMessage(player, "§c❌ Unknown subcommand: " .. tostring(sub))

end)