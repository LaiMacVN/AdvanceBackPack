package me.lightmax.advancebackpack.menus;

import me.lightmax.advancebackpack.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public abstract class MenuManager implements InventoryHolder {

    protected Inventory inventory;
    protected Main plugin;


    protected PlayerMenuUtility playerMenuUtility;

    public MenuManager(Main plugin, PlayerMenuUtility playerMenuUtility) {
        this.plugin = plugin;
        this.playerMenuUtility = playerMenuUtility;
    }

    public abstract String getInventoryName();

    public abstract int getInventorySize();

    public abstract void handlersClick(InventoryClickEvent event);

    public abstract void handlersClose(InventoryCloseEvent event);

    public abstract void handlersItemsSlot(Player player);

    public void open() {
        inventory = Bukkit.createInventory(this, getInventorySize(), getInventoryName());

        this.handlersItemsSlot(playerMenuUtility.getOwner());

        playerMenuUtility.getOwner().openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setDecorate(int[] slots, String displayName, String... lore) {
        for (Integer num : slots) {
            ItemStack itemStack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null) {
                return;
            }
            meta.setDisplayName(displayName);
            meta.setLore(Arrays.asList(lore));
            itemStack.setItemMeta(meta);
            inventory.setItem(num, itemStack);
        }
    }

    public void fillAllAirToGlass(int guiSize, String displayName, String... lore) {
        for (int i = 0; i < guiSize; i++) {
            if (inventory.getItem(i) == null) {
                ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
                ItemMeta meta = item.getItemMeta();
                if (meta == null) {
                    return;
                }
                meta.setDisplayName(displayName);
                meta.setLore(Arrays.asList(lore));
                item.setItemMeta(meta);
                inventory.setItem(i, item);
            }
        }
    }


}
