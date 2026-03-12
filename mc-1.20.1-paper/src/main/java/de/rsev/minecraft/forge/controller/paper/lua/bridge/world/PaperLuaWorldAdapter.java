package de.rsev.minecraft.forge.controller.paper.lua.bridge.world;

import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerBridge;
import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerWrapper;
import de.rsev.minecraft.forge.controller.lua.bridge.world.LuaWorldAdapter;
import de.rsev.minecraft.forge.controller.paper.lua.bridge.player.PaperLuaPlayerHandle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.Locale;

public class PaperLuaWorldAdapter implements LuaWorldAdapter {

    @Override
    public boolean setBlock(int x, int y, int z, String blockId) {
        Material material = Material.matchMaterial(blockId);
        if (material == null || world() == null) {
            return false;
        }

        world().getBlockAt(x, y, z).setType(material);
        return true;
    }

    @Override
    public String getBlock(int x, int y, int z) {
        if (world() == null) {
            return "minecraft:air";
        }

        Material material = world().getBlockAt(x, y, z).getType();
        NamespacedKey key = material.getKey();
        return key != null ? key.toString() : material.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public boolean setSignText(int x, int y, int z, String l1, String l2) {
        if (world() == null) {
            return false;
        }

        Block block = world().getBlockAt(x, y, z);
        if (!(block.getState() instanceof Sign sign)) {
            return false;
        }

        sign.line(0, net.kyori.adventure.text.Component.text(l1));
        sign.line(1, net.kyori.adventure.text.Component.text(l2));
        sign.update();
        return true;
    }

    @Override
    public boolean spawnEntity(String id, double x, double y, double z) {
        if (world() == null) {
            return false;
        }

        EntityType type = resolveEntityType(id);
        if (type == null) {
            return false;
        }

        return world().spawnEntity(new Location(world(), x, y, z), type) != null;
    }

    @Override
    public LuaTable getAllEntities() {
        LuaTable table = new LuaTable();
        int index = 1;
        if (world() == null) {
            return table;
        }

        for (Entity entity : world().getEntities()) {
            table.set(index++, entityToLua(entity));
        }

        return table;
    }

    @Override
    public LuaTable getEntities(String type, double x, double y, double z, double range) {
        LuaTable table = new LuaTable();
        int index = 1;
        if (world() == null) {
            return table;
        }

        Iterable<Entity> entities = shouldUseNearbySearch(x, y, z, range)
                ? world().getNearbyEntities(new Location(world(), x, y, z), range, range, range)
                : world().getEntities();

        for (Entity entity : entities) {
            if (matchesEntityType(entity, type)) {
                table.set(index++, entityToLua(entity));
            }
        }

        return table;
    }

    @Override
    public boolean removeEntity(int id) {
        Entity entity = findEntity(id);
        if (entity == null) {
            return false;
        }

        entity.remove();
        return true;
    }

    @Override
    public int killAllEntities() {
        int count = 0;
        if (world() == null) {
            return 0;
        }

        for (Entity entity : world().getEntities()) {
            if (!(entity instanceof Player)) {
                entity.remove();
                count++;
            }
        }
        return count;
    }

    @Override
    public int killEntities(LuaTable filter) {
        String type = filter.get("type").optjstring(null);
        double x = filter.get("x").optdouble(Double.NaN);
        double y = filter.get("y").optdouble(Double.NaN);
        double z = filter.get("z").optdouble(Double.NaN);
        double range = filter.get("range").optdouble(0);
        int count = 0;
        if (world() == null) {
            return 0;
        }

        for (Entity entity : world().getEntities()) {
            if (entity instanceof Player) {
                continue;
            }
            if (!matchesEntityType(entity, type)) {
                continue;
            }
            if (!isWithinRange(entity, x, y, z, range)) {
                continue;
            }

            entity.remove();
            count++;
        }
        return count;
    }

    @Override
    public LuaTable getPlayers() {
        LuaTable table = new LuaTable();
        int index = 1;
        for (Player player : Bukkit.getOnlinePlayers()) {
            table.set(index++, LuaPlayerBridge.wrap(new LuaPlayerWrapper(new PaperLuaPlayerHandle(player))));
        }
        return table;
    }

    @Override
    public Object getPlayerById(int id) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getEntityId() == id) {
                return LuaPlayerBridge.wrap(new LuaPlayerWrapper(new PaperLuaPlayerHandle(player)));
            }
        }
        return null;
    }

    @Override
    public long getWorldTime() {
        return world() != null ? world().getTime() : 0;
    }

    @Override
    public boolean setWorldTime(long time) {
        if (world() == null) {
            return false;
        }
        world().setTime(time);
        return true;
    }

    @Override
    public String getWeather() {
        if (world() == null) {
            return "clear";
        }
        if (world().isThundering()) {
            return "thunder";
        }
        if (world().hasStorm()) {
            return "rain";
        }
        return "clear";
    }

    @Override
    public boolean setWeather(String type) {
        if (world() == null || type == null) {
            return false;
        }

        String normalized = type.toUpperCase(Locale.ROOT);
        switch (normalized) {
            case "CLEAR" -> {
                world().setStorm(false);
                world().setThundering(false);
                return true;
            }
            case "RAIN" -> {
                world().setStorm(true);
                world().setThundering(false);
                return true;
            }
            case "THUNDER" -> {
                world().setStorm(true);
                world().setThundering(true);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    private boolean shouldUseNearbySearch(double x, double y, double z, double range) {
        return !Double.isNaN(x)
                && !Double.isNaN(y)
                && !Double.isNaN(z)
                && range > 0;
    }

    private boolean isWithinRange(Entity entity, double x, double y, double z, double range) {
        if (!shouldUseNearbySearch(x, y, z, range)) {
            return true;
        }

        Location location = entity.getLocation();
        double dx = location.getX() - x;
        double dy = location.getY() - y;
        double dz = location.getZ() - z;
        return dx * dx + dy * dy + dz * dz <= range * range;
    }

    private boolean matchesEntityType(Entity entity, String requestedType) {
        if (requestedType == null || requestedType.isBlank()) {
            return true;
        }

        EntityType entityType = entity.getType();
        String normalizedRequested = requestedType.trim().toLowerCase(Locale.ROOT);
        String entityName = entityType.name().toLowerCase(Locale.ROOT);
        NamespacedKey key = entityType.getKey();
        String namespaced = key != null ? key.toString().toLowerCase(Locale.ROOT) : entityName;
        String simpleKey = namespaced.contains(":")
                ? namespaced.substring(namespaced.indexOf(':') + 1)
                : namespaced;

        return normalizedRequested.equals(entityName)
                || normalizedRequested.equals(namespaced)
                || normalizedRequested.equals(simpleKey);
    }

    private EntityType resolveEntityType(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }

        String normalized = id.trim().toLowerCase(Locale.ROOT);
        String simpleId = normalized.startsWith("minecraft:")
                ? normalized.substring("minecraft:".length())
                : normalized;

        EntityType type = EntityType.fromName(simpleId);
        if (type != null) {
            return type;
        }

        try {
            return EntityType.valueOf(simpleId.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    private LuaTable entityToLua(Entity entity) {
        LuaTable table = new LuaTable();
        table.set("id", LuaValue.valueOf(entity.getEntityId()));
        NamespacedKey key = entity.getType().getKey();
        String typeId = key != null
                ? key.toString()
                : entity.getType().name().toLowerCase(Locale.ROOT);
        table.set("type", LuaValue.valueOf(typeId));
        table.set("x", LuaValue.valueOf(entity.getLocation().getX()));
        table.set("y", LuaValue.valueOf(entity.getLocation().getY()));
        table.set("z", LuaValue.valueOf(entity.getLocation().getZ()));
        return table;
    }

    private Entity findEntity(int id) {
        if (world() == null) {
            return null;
        }

        for (Entity entity : world().getEntities()) {
            if (entity.getEntityId() == id) {
                return entity;
            }
        }
        return null;
    }

    private World world() {
        return Bukkit.getWorlds().isEmpty() ? null : Bukkit.getWorlds().get(0);
    }
}
