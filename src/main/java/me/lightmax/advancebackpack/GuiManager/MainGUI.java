package me.lightmax.advancebackpack.GuiManager;

import me.lightmax.advancebackpack.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public class MainGUI {

    Main plugin;

    public MainGUI(Main plugin) {
        this.plugin = plugin;
    }


    public void openInventory(Player player) {
        Inventory inv = Bukkit.createInventory(player, 54, "Lưu trữ!");
        setDecorateItem(Material.GRAY_STAINED_GLASS_PANE, 0, 9, "", inv);
        setDecorateItem(Material.GRAY_STAINED_GLASS_PANE, 18, 27, "", inv);
        for(int i = 9; i < 12; i++) {
            setItem(inv, Material.GREEN_STAINED_GLASS_PANE, i, "Kho đồ " + (i - 8));
        }

        player.openInventory(inv);
    }

    public void setDecorateItem(Material material, int slotMin, int slotMax, String name, Inventory inv) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            return;
        }
        meta.setDisplayName(name);
        for(int i = slotMin; i < slotMax; i++) {
            inv.setItem(i, item);
        }
    }
    public void setItem(Inventory inv, Material material, int slot, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if(meta == null) {
            return;
        }
        meta.setDisplayName(name);
        inv.setItem(slot, item);
    }
}
