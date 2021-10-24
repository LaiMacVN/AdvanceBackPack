package me.lightmax.advancebackpack.menus.menu;

import me.lightmax.advancebackpack.ItemStackSerializer;
import me.lightmax.advancebackpack.Main;
import me.lightmax.advancebackpack.menus.MenuManager;
import me.lightmax.advancebackpack.menus.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;


public class MainGUI extends MenuManager {


    public MainGUI(Main plugin, PlayerMenuUtility playerMenuUtility) {
        super(plugin, playerMenuUtility);
    }

    @Override
    public String getInventoryName() {
        return "Lưu trữ";
    }

    @Override
    public int getInventorySize() {
        return 27;
    }

    @Override
    public void handlersClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();

        if (plugin.isEnableMySQl) {
            switch (event.getCurrentItem().getType()) {
                case RED_STAINED_GLASS_PANE:
                    player.sendMessage(plugin.utils.color("&cBạn chưa mở khóa ô này! &o&7(/backpack unlock để mở khóa!)"));
                    break;
                case GREEN_STAINED_GLASS_PANE:
                    int name = plugin.utils.itemNameSerializeToInt(event.getCurrentItem().getItemMeta().getDisplayName());
                    if (name == 0 || name > 9) {
                        return;
                    }
                    Inventory inv = Bukkit.createInventory(null, 54, "Kho đồ " + name + " của " + player.getName());
                    if (!plugin.data.isPlayerExist(name, player.getUniqueId())) {
                        plugin.data.createPlayer(name, player);
                        player.openInventory(inv);
                        return;
                    }
                    try {
                        inv.setContents(ItemStackSerializer.convertitemStackArrayFromBase64(plugin.data.getInventoryContent(name, player.getUniqueId())));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.openInventory(inv);
                    break;
            }
            return;
        }

        if (plugin.isEnableYAMLStorage) {
            switch (event.getCurrentItem().getType()) {
                case RED_STAINED_GLASS_PANE:
                    player.sendMessage(plugin.utils.color("&cBạn chưa mở khóa ô này! &o&7(/backpack unlock để mở khóa!)"));
                    break;
                case GREEN_STAINED_GLASS_PANE:
                    int name = plugin.utils.itemNameSerializeToInt(event.getCurrentItem().getItemMeta().getDisplayName());
                    if (name == 0 || name > 9) {
                        return;
                    }
                    Inventory inv = Bukkit.createInventory(null, 54, "Kho đồ " + name + " của " + player.getName());
                    if (plugin.yamlStorage.isExistPlayer(player.getUniqueId(), "/data")) {
                        plugin.yamlStorage.createStorage(player.getUniqueId() + ".yml", "/data");
                        player.openInventory(inv);
                        return;
                    }
                    try {
                        inv.setContents(ItemStackSerializer.convertitemStackArrayFromBase64(
                                plugin.yamlStorage.getConfig(
                                                player.getUniqueId() + ".yml",
                                                "/data")
                                        .getString(name +
                                                "." + player.getName() +
                                                "." + player.getUniqueId()
                                        )));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.openInventory(inv);
                    break;
            }
        }

    }

    @Override
    public void handlersClose(InventoryCloseEvent event) {
        //owo
    }

    @Override
    public void handlersItemsSlot(Player player) {
        int num = plugin.utils.checkPlayerBackPackAmount(9, "AdvanceBackPack.use.", player);
        setDecorate(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 18, 19, 20, 21, 22, 23, 24, 25, 26}, "");
        for (int i = 9; i < (9 + num); i++) {
            ItemStack owned = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
            ItemMeta ownedMeta = owned.getItemMeta();
            if (ownedMeta == null) {
                return;
            }
            ownedMeta.setDisplayName(plugin.utils.color("&aKho đồ " + (i - 8)));
            owned.setItemMeta(ownedMeta);
            inventory.setItem(i, owned);
        }

        for (int i = 0; i < 27; i++) {
            if (inventory.getItem(i) == null) {
                ItemStack NotOwned = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta NotOwnedMeta = NotOwned.getItemMeta();
                if (NotOwnedMeta == null) {
                    return;
                }
                NotOwnedMeta.setDisplayName(plugin.utils.color("&cChưa mở khóa! &0&7(/backpack unlock)"));
                NotOwned.setItemMeta(NotOwnedMeta);
                inventory.setItem(i, NotOwned);
            }
        }
    }
}
