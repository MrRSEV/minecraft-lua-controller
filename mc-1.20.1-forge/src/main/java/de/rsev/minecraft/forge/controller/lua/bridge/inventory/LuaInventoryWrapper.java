package de.rsev.minecraft.forge.controller.lua.bridge.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;

public class LuaInventoryWrapper {

    private final Player player;

    public LuaInventoryWrapper(Player player) {
        this.player = player;
    }

    public boolean addItem(String itemId, int count) {

        ResourceLocation id = ResourceLocation.tryParse(itemId);
        if (id == null) return false;

        Item item = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(id);
        if (item == null || item == net.minecraft.world.item.Items.AIR) return false;

        ItemStack stack = new ItemStack(item, count);
        return player.getInventory().add(stack);

    }

    public boolean removeItem(String itemId, int count) {

        ResourceLocation id = ResourceLocation.tryParse(itemId);

        if (id == null) return false;

        Item item = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(id);
        if (item == null || item == net.minecraft.world.item.Items.AIR) return false;

        int removed = player.getInventory().clearOrCountMatchingItems(
                stack -> stack.getItem() == item,
                count,
                player.inventoryMenu.getCraftSlots()
        );

        return removed > 0;

    }

    public int getItemCount(String itemId) {

        ResourceLocation id = ResourceLocation.tryParse(itemId);
        if (id == null) return 0;

        Item item = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(id);
        if (item == null || item == net.minecraft.world.item.Items.AIR) return 0;

        return player.getInventory().countItem(item);

    }

    public void clear() {
        player.getInventory().clearContent();
    }

    public Player getHandle() {
        return player;
    }
}
