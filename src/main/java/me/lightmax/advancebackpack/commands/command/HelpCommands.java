package me.lightmax.advancebackpack.commands.command;

import me.lightmax.advancebackpack.Main;
import me.lightmax.advancebackpack.commands.CommandManager;
import me.lightmax.advancebackpack.commands.SubCommands;
import org.bukkit.entity.Player;

import java.util.Objects;

public class HelpCommands extends SubCommands {

    private final CommandManager commandManager;
    private final Main plugin;

    public HelpCommands(Main plugin, CommandManager commandManager) {
        this.plugin = plugin;
        this.commandManager = commandManager;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDesc() {
        return "Hiện ra bảng trợ giúp của plugins";
    }

    @Override
    public String getSyntax() {
        return "/backpack help [lệnh]";
    }

    @Override
    public String getPermission() {
        return "default";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length == 1) {
            player.sendMessage(plugin.utils.color("&e&l&m----------------------------------"));
            for (int i = 0; i < commandManager.getCommands().size(); i++) {
                if (player.hasPermission(commandManager.getCommands().get(i).getPermission()) || Objects.equals(commandManager.getCommands().get(i).getPermission(), "default")) {
                    player.sendMessage(plugin.utils.color("&b" + commandManager.getCommands().get(i).getSyntax() + "&7: " + commandManager.getCommands().get(i).getDesc()));
                }
            }
            player.sendMessage(plugin.utils.color("&e&l&m----------------------------------"));
        }
        if (args.length > 1) {
            for (int i = 0; i < commandManager.getCommands().size(); i++) {
                if (args[1].equalsIgnoreCase(commandManager.getCommands().get(i).getName())) {
                    if (player.hasPermission(commandManager.getCommands().get(i).getPermission()) || Objects.equals(commandManager.getCommands().get(i).getPermission(), "default")) {
                        player.sendMessage(plugin.utils.color("&a" + commandManager.getCommands().get(i).getDesc()));
                    } else {
                        player.sendMessage(plugin.utils.color("&cBạn không có quyền để xem trợ giúp lệnh này!"));
                    }
                    break;
                }
            }
        }
    }
}
