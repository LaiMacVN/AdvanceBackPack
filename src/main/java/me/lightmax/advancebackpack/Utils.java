package me.lightmax.advancebackpack;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    Main plugin;

    public Utils(Main plugin) {
        this.plugin = plugin;
    }

    public String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    public String decolor(String s) {
        return ChatColor.stripColor(s);
    }

    public int getTime(double time) {
        return (int) (System.currentTimeMillis() - time);
    }

    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            //0ww0
        }
        return false;
    }

    public int menuTitleSerializeToInt(String title) {
        String numberOnly = title.replaceAll("[^0-9]", "");
        if (isInt(numberOnly)) {
            return Integer.parseInt(numberOnly);
        }
        return 0;
    }

    public int itemNameSerializeToInt(String itemName) {
        String numberOnly = itemName.replaceAll("[^0-9]", "");
        if (isInt(numberOnly)) {
            return Integer.parseInt(numberOnly);
        }
        return 0;
    }

    public int checkPlayerBackPackAmount(int backPackMaxSize, String permissionNode, Player player) {

        int j = 0;
        for (int i = 1; i <= backPackMaxSize; i++) {
            if (plugin.getPerms().playerHas(player, permissionNode + i)) {
                j = i;
            }
        }
        if (j == 0) {
            plugin.getPerms().playerAdd(player, permissionNode + ".3");
            return 3;
        }
        return j;
    }


    public ItemStack itemStack(int amount, int money) {
        ItemStack itemStack = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(plugin.utils.color("&bNâng cấp!"));
        List<String> lore = new ArrayList<>();
        lore.add(plugin.utils.color("&7Nâng cấp số lượng kho đồ lên " + amount + "!"));
        lore.add("");
        lore.add(plugin.utils.color("&aGiá: &e" + money + "&e$"));
        lore.add("");
        lore.add(plugin.utils.color("&eNhấn để nâng cấp!"));
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack makeItem() {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(plugin.utils.color("&c&lCHƯA THỂ NÂNG CẤP!"));
        List<String> lore = new ArrayList<>();
        lore.add(plugin.utils.color("&7Hãy nâng cấp những cấp độ khác trước"));
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public ItemStack upgradedItem() {
        ItemStack itemStack = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(plugin.utils.color("&a&lBẠN ĐÃ NÂNG CẤP!"));
        List<String> lore = new ArrayList<>();
        lore.add(plugin.utils.color("&eBạn đã nâng cấp cấp độ này rồi!"));
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
