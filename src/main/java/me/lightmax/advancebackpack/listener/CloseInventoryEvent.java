package me.lightmax.advancebackpack.listener;

import me.lightmax.advancebackpack.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class CloseInventoryEvent implements Listener {

    Main plugin;

    public CloseInventoryEvent(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if(e.getView().getTitle().contains(e.getPlayer().getName())) {
            String title = e.getView().getTitle().replace(" ", "");
            char[] chars = title.toCharArray();
            StringBuilder sb = new StringBuilder();
            for(char c : chars){
                if(Character.isDigit(c)){
                    sb.append(c);
                }
            }
            int num = Integer.parseInt(sb.toString());
            plugin.data.setInventoryContent(num, p.getUniqueId(), e.getInventory().getContents());
        }
    }
}
