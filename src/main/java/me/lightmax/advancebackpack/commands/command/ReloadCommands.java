package me.lightmax.advancebackpack.commands.command;

import me.lightmax.advancebackpack.Main;
import me.lightmax.advancebackpack.commands.SubCommands;
import org.bukkit.entity.Player;

public class ReloadCommands extends SubCommands {

    Main plugin;

    public ReloadCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDesc() {
        return "Tải lại plugin!";
    }

    @Override
    public String getSyntax() {
        return "/backpack reload";
    }

    @Override
    public String getPermission() {
        return "advancebackpack.reload";
    }

    @Override
    public void perform(Player player, String[] args) {
        double time = System.currentTimeMillis();
        plugin.reloadConfig();
        player.sendMessage(plugin.utils.color("&aĐã reload config thành công tốn " + plugin.utils.getTime(time)) + "&ams");
    }
}
