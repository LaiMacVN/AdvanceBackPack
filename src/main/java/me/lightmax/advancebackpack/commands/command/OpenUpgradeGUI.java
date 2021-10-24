package me.lightmax.advancebackpack.commands.command;

import me.lightmax.advancebackpack.Main;
import me.lightmax.advancebackpack.commands.SubCommands;
import me.lightmax.advancebackpack.menus.menu.UpgradeGUI;
import org.bukkit.entity.Player;

public class OpenUpgradeGUI extends SubCommands {
    Main plugin;

    public OpenUpgradeGUI(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "unlock";
    }

    @Override
    public String getDesc() {
        return "Nâng cấp số lượng backpack của bạn!";
    }

    @Override
    public String getSyntax() {
        return "/backpack unlock";
    }

    @Override
    public String getPermission() {
        return "default";
    }

    @Override
    public void perform(Player player, String[] args) {
        new UpgradeGUI(plugin, plugin.getPlayerMenuUtility(player)).open();
    }
}
