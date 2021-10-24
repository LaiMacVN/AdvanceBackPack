package me.lightmax.advancebackpack.commands.command;

import me.lightmax.advancebackpack.Main;
import me.lightmax.advancebackpack.commands.SubCommands;
import me.lightmax.advancebackpack.menus.menu.MainGUI;
import org.bukkit.entity.Player;

public class OpenGuiCommand extends SubCommands {

    Main plugin;

    public OpenGuiCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "open";
    }

    @Override
    public String getDesc() {
        return "Mở menu backpack chính";
    }

    @Override
    public String getSyntax() {
        return "/backpack [open]";
    }

    @Override
    public String getPermission() {
        return "default";
    }

    @Override
    public void perform(Player player, String[] args) {
        new MainGUI(plugin, plugin.getPlayerMenuUtility(player)).open();
    }
}
