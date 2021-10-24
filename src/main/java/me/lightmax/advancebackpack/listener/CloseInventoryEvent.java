package me.lightmax.advancebackpack.listener;

import me.lightmax.advancebackpack.ItemStackSerializer;
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
        if (e.getView().getTitle().contains(e.getPlayer().getName())) {
            String title = plugin.utils.decolor(e.getView().getTitle());
            int num = plugin.utils.menuTitleSerializeToInt(title);
            if (num == 0 || num > 9) {
                return;
            }
            if (plugin.isEnableMySQl) {
                if (!plugin.data.isPlayerExist(num, p.getUniqueId())) {
                    plugin.data.createPlayer(num, p);
                }
                plugin.data.setInventoryContent(num, p.getUniqueId(), e.getInventory().getContents());
                return;
            }

            if (plugin.isEnableYAMLStorage) {
                if (plugin.yamlStorage.isExistPlayer(p.getUniqueId(), "/data")) {
                    plugin.yamlStorage.createStorage(p.getUniqueId().toString(), "/data");
                }
                if (!plugin.yamlStorage.getConfig(p.getUniqueId().toString(), "/data").isConfigurationSection(String.valueOf(num))) {
                    plugin.yamlStorage.getConfig(p.getUniqueId().toString(),
                                    "/data")
                            .createSection(
                                    num +
                                            "." + p.getName() +
                                            "." + p.getUniqueId()
                            );
                }
                plugin.yamlStorage.getConfig(p.getUniqueId().toString(), "/data").set(num + "." + p.getName() + "." + p.getUniqueId(), ItemStackSerializer.convertitemStackArrayToBase64(e.getInventory().getContents()));

            }

        }
    }
}
