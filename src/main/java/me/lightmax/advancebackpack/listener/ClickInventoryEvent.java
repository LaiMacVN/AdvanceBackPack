package me.lightmax.advancebackpack.listener;

import me.lightmax.advancebackpack.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickInventoryEvent implements Listener {

    Main plugin;

    public ClickInventoryEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getCurrentItem() == null) return;
        if(e.getCurrentItem().getItemMeta() == null) return;
        if(e.getView().getTitle().contains("Lưu trữ!")) {
            e.setCancelled(true);
            if(e.getSlot() == 9) {
                if(plugin.data.isExist(1, p.getUniqueId())) {

                }
            }
        }
    }



}
