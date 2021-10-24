package me.lightmax.advancebackpack.commands;

import me.lightmax.advancebackpack.Main;
import me.lightmax.advancebackpack.commands.command.*;
import me.lightmax.advancebackpack.menus.menu.MainGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandManager implements TabExecutor {

    Main plugin;

    private final ArrayList<SubCommands> commands = new ArrayList<>();

    public CommandManager(Main plugin) {
        this.plugin = plugin;
        commands.add(new ReloadCommands(plugin));
        commands.add(new OpenGuiCommand(plugin));
        commands.add(new HelpCommands(plugin, this));
        commands.add(new DeleteBackPackCommand(plugin));
        commands.add(new DebugCommands(plugin));
        commands.add(new OpenUpgradeGUI(plugin));
    }

    public ArrayList<SubCommands> getCommands() {
        return commands;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;


            if (args.length == 0) {
                new MainGUI(plugin, plugin.getPlayerMenuUtility(p)).open();
            }

            if (args.length > 0) {
                for (int i = 0; i < getCommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getCommands().get(i).getName())) {
                        if (p.hasPermission(getCommands().get(i).getPermission()) || Objects.equals(getCommands().get(i).getPermission(), "default")) {
                            if (plugin.commandCooldowns.checkCooldown(p)) {
                                getCommands().get(i).perform(p, args);
                                plugin.commandCooldowns.setCooldowns(p, 2);
                            } else {
                                p.sendMessage(plugin.utils.color("&cBạn đang dùng lệnh quá nhanh đấy! Chờ 1 tí nào!"));
                                return true;
                            }
                        } else {
                            p.sendMessage(plugin.utils.color("&cBạn không có quyền để dùng lệnh này!"));
                            return true;
                        }
                        break;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> commandsList = new ArrayList<>();
        for (SubCommands subCommands : commands) {
            if (sender.hasPermission(subCommands.getPermission()) || subCommands.getPermission().equalsIgnoreCase("default")) {
                commandsList.add(subCommands.getName());
            }
        }
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            for (String a : commandsList) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase()))
                    result.add(a);
            }
            return result;
        }

        return null;
    }
}
