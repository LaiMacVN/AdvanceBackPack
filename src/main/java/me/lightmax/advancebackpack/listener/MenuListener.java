package me.lightmax.advancebackpack.listener;

import me.lightmax.advancebackpack.Main;
import me.lightmax.advancebackpack.menus.MenuManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

    Main plugin;

    public MenuListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();

        Player player = (Player) e.getWhoClicked();

        if (holder instanceof MenuManager) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) {
                return;
            }
            if (e.getCurrentItem().getItemMeta() == null) return;

            MenuManager menuManager = (MenuManager) holder;

            if (plugin.Clickcooldown.checkCooldown(player)) {
                menuManager.handlersClick(e);
                plugin.Clickcooldown.setCooldowns(player, 1);
            } else {
                player.sendMessage(plugin.utils.color("&cBạn đang bấm quá nhanh đấy! Chờ 1 tí nào!"));
            }

        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();

        if (holder instanceof MenuManager) {
            MenuManager menuManager = (MenuManager) holder;

            menuManager.handlersClose(e);
        }
    }
}
