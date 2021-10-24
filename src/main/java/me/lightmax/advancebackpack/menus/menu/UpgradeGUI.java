package me.lightmax.advancebackpack.menus.menu;

import me.lightmax.advancebackpack.Main;
import me.lightmax.advancebackpack.menus.MenuManager;
import me.lightmax.advancebackpack.menus.PlayerMenuUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class UpgradeGUI extends MenuManager {

    Main plugin;

    public UpgradeGUI(Main plugin, PlayerMenuUtility playerMenuUtility) {
        super(plugin, playerMenuUtility);
        this.plugin = plugin;
    }

    @Override
    public String getInventoryName() {
        return "Nâng Cấp Kho Đồ";
    }

    @Override
    public int getInventorySize() {
        return 45;
    }

    @Override
    public void handlersClick(InventoryClickEvent event) {
        //handlers player click event here
        Player player = (Player) event.getWhoClicked();

        switch (event.getSlot()) {
            case 19:
                switch (event.getCurrentItem().getType()) {
                    case NETHER_STAR:
                        plugin.buyUpgradeMethod(player, 5000000, 4);
                        new UpgradeGUI(plugin, plugin.getPlayerMenuUtility(player)).open();
                        break;
                    case GREEN_STAINED_GLASS_PANE:
                        player.sendMessage(plugin.utils.color("&eBạn đã nâng cấp cấp độ này rồi!"));
                        break;
                }
                break;
            case 21:
                switch (event.getCurrentItem().getType()) {
                    case NETHER_STAR:
                        plugin.buyUpgradeMethod(player, 10000000, 5);
                        new UpgradeGUI(plugin, plugin.getPlayerMenuUtility(player)).open();
                        break;
                    case BARRIER:
                        player.sendMessage(plugin.utils.color("&cBạn cần phải nâng cấp từng cấp độ!"));
                        break;
                    case GREEN_STAINED_GLASS_PANE:
                        player.sendMessage(plugin.utils.color("&eBạn đã nâng cấp cấp độ này rồi!"));
                        break;
                }
                break;
            case 23:
                switch (event.getCurrentItem().getType()) {
                    case NETHER_STAR:
                        plugin.buyUpgradeMethod(player, 20000000, 7);
                        new UpgradeGUI(plugin, plugin.getPlayerMenuUtility(player)).open();
                        break;
                    case BARRIER:
                        player.sendMessage(plugin.utils.color("&cBạn cần phải nâng cấp từng cấp độ!"));
                        break;
                    case GREEN_STAINED_GLASS_PANE:
                        player.sendMessage(plugin.utils.color("&eBạn đã nâng cấp cấp độ này rồi!"));
                        break;
                }
                break;
            case 25:
                switch (event.getCurrentItem().getType()) {
                    case NETHER_STAR:
                        plugin.buyUpgradeMethod(player, 40000000, 9);
                        new UpgradeGUI(plugin, plugin.getPlayerMenuUtility(player)).open();
                        break;
                    case BARRIER:
                        player.sendMessage(plugin.utils.color("&cBạn cần phải nâng cấp từng cấp độ!"));
                        break;
                    case GREEN_STAINED_GLASS_PANE:
                        player.sendMessage(plugin.utils.color("&eBạn đã nâng cấp cấp độ này rồi!"));
                        break;
                }
                break;
        }
    }

    @Override
    public void handlersClose(InventoryCloseEvent event) {
        //owo
    }

    @Override
    public void handlersItemsSlot(Player player) {
        inventory.setItem(19, plugin.utils.itemStack(4, 50000000));
        if (player.hasPermission("AdvanceBackPack.use.4")) {
            inventory.setItem(21, plugin.utils.itemStack(5, 100000000));
        } else {
            inventory.setItem(21, plugin.utils.makeItem());
        }
        if (player.hasPermission("AdvanceBackPack.use.5")) {
            inventory.setItem(23, plugin.utils.itemStack(7, 200000000));
        } else {
            inventory.setItem(23, plugin.utils.makeItem());
        }

        if (player.hasPermission("AdvanceBackPack.use.7")) {
            inventory.setItem(25, plugin.utils.itemStack(9, 400000000));
        } else {
            inventory.setItem(25, plugin.utils.makeItem());
        }

        fillAllAirToGlass(45, "");
    }
}
