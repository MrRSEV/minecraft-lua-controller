package de.rsev.minecraft.forge.controller.lua.bridge.inventory;

import de.rsev.minecraft.forge.controller.logging.LuaLogger;
import de.rsev.minecraft.forge.controller.lua.bridge.player.LuaPlayerLookup;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class LuaInventoryBridgeInternal {

    // =========================
    // Helpers
    // =========================
    private static ServerPlayer getPlayer(int playerId) {
        return LuaPlayerLookup.find(playerId);
    }

    // =========================
    // API
    // =========================
    public static boolean addItem(int playerId, String itemId, int count) {

        LuaLogger.info("Lua → Inventory.addItem player={} item={} count={}", playerId, itemId, count);

        if (itemId == null || itemId.isBlank() || count <= 0) {
            LuaLogger.warn("Inventory.addItem skipped → invalid params");
            return false;
        }

        try {
            ServerPlayer player = getPlayer(playerId);
            if (player == null) {
                LuaLogger.warn("Inventory.addItem → player {} not found", playerId);
                return false;
            }

            ResourceLocation id = ResourceLocation.tryParse(itemId);

            if (id == null) {
                LuaLogger.warn("Inventory.addItem → invalid ResourceLocation {}", itemId);
                return false;
            }

            Item item = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(id);

            if (item == null || item == net.minecraft.world.item.Items.AIR) {
                LuaLogger.warn("Inventory.addItem → unknown itemId {}", itemId);
                return false;
            }



            ItemStack stack = new ItemStack(item, count);
            return player.getInventory().add(stack);

        } catch (Exception e) {
            LuaLogger.error("Inventory.addItem failed", e);
            return false;
        }
    }


    public static boolean removeItem(int playerId, String itemId, int count) {

        LuaLogger.info("Lua → Inventory.removeItem player={} item={} count={}", playerId, itemId, count);

        if (itemId == null || itemId.isBlank() || count <= 0) {
            LuaLogger.warn("Inventory.removeItem skipped → invalid params");
            return false;
        }

        try {
            ServerPlayer player = getPlayer(playerId);
            if (player == null) {
                LuaLogger.warn("Inventory.removeItem → player {} not found", playerId);
                return false;
            }

            ResourceLocation id = ResourceLocation.tryParse(itemId);

            if (id == null) {
                LuaLogger.warn("Inventory.removeItem → invalid ResourceLocation {}", itemId);
                return false;
            }

            Item item = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(id);

            if (item == null || item == net.minecraft.world.item.Items.AIR) {
                LuaLogger.warn("Inventory.removeItem → unknown itemId {}", itemId);
                return false;
            }

            int removed = player.getInventory().clearOrCountMatchingItems(
                    stack -> stack.getItem() == item,
                    count,
                    player.inventoryMenu.getCraftSlots()
            );

            LuaLogger.info("Inventory.removeItem → removed {}x {}", removed, itemId);

            return removed > 0;


        } catch (Exception e) {
            LuaLogger.error("Inventory.removeItem failed", e);
            return false;
        }
    }

    public static boolean addItemToBlock(int x, int y, int z, String itemId, int count) {

        LuaLogger.info("Lua → Inventory.addItemToBlock {} {} {} item={} count={}", x, y, z, itemId, count);

        try {
            
            var player = LuaPlayerLookup.find(0);
            if (player == null) return false;
            
            var level = player.serverLevel();
            var pos = new BlockPos(x, y, z);
            var be = level.getBlockEntity(pos);

            if (!(be instanceof Container container)) {
                LuaLogger.warn("Inventory.addItemToBlock → no container at {} {} {}", x, y, z);
                return false;
            }

            var itemRL = ResourceLocation.tryParse(itemId);
            if (itemRL == null) return false;

            var item = ForgeRegistries.ITEMS.getValue(itemRL);
            if (item == null) return false;

            ItemStack stack = new ItemStack(item, count);

            for (int i = 0; i < container.getContainerSize(); i++) {

                ItemStack slot = container.getItem(i);

                if (slot.isEmpty()) {
                    container.setItem(i, stack);
                    container.setChanged();
                    return true;
                }
            }

            LuaLogger.warn("Inventory.addItemToBlock → container full");
            return false;

        } catch (Exception e) {
            LuaLogger.error("Inventory.addItemToBlock failed", e);
            return false;
        }
    }


    public static LuaInventoryWrapper getInventory(int playerId) {

        ServerPlayer player = getPlayer(playerId);
        if (player == null) return null;

        return new LuaInventoryWrapper(player);
    }
}
