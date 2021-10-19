package me.lightmax.advancebackpack.commands;

import me.lightmax.advancebackpack.ItemStackSerializer;
import me.lightmax.advancebackpack.Main;
import me.lightmax.advancebackpack.commands.command.ReloadCommands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.ArrayList;

public class CommandManager implements CommandExecutor {

    Main plugin;

    private ArrayList<SubCommands> commands = new ArrayList<>();

    public CommandManager(Main plugin) {
        this.plugin = plugin;
        commands.add(new ReloadCommands(plugin));
    }

    public ArrayList<SubCommands> getCommands() {
        return commands;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(args.length == 1 && args[0].equalsIgnoreCase("help")) {
                p.sendMessage(plugin.utils.color("&e&l&m----------------------------------"));
                for (int i = 0; i< getCommands().size(); i++) {
                    if(p.hasPermission(getCommands().get(i).getPermission())) {
                        p.sendMessage(plugin.utils.color("&b" + getCommands().get(i).getSyntax() + "&7: " + getCommands().get(i).getDesc()));
                    }
                }
                p.sendMessage(plugin.utils.color("&e&l&m----------------------------------"));
            }

            if(args.length == 1) {
                if(plugin.utils.isInt(args[0])) {
                    int num = Integer.parseInt(args[0]);
                    if(num == 1 || num == 2 || num == 3) {

                        Inventory inv = Bukkit.createInventory(p, 54, "Kho đồ " + num + " của " + p.getName());
                        try {
                            inv.setContents(ItemStackSerializer.convertitemStackArrayFromBase64(plugin.data.getInventoryContent(num, p.getUniqueId())));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        p.openInventory(inv);

                    }else{
                        p.sendMessage(plugin.utils.color("&cNhập id kho đồ từ 1-3"));
                    }
                }else{
                    p.sendMessage(plugin.utils.color("&cNhập id kho đồ từ 1-3"));
                }

            }

            if(args.length > 0) {
                for (int i = 0; i < getCommands().size(); i++) {
                    if(args[0].equalsIgnoreCase(getCommands().get(i).getName())) {
                        if(p.hasPermission(getCommands().get(i).getPermission())) {
                            getCommands().get(i).perform(p, args);
                        }else{
                            p.sendMessage(plugin.utils.color("&cBạn không có quyền để dùng lệnh này!"));
                        }
                        break;
                    }
                }
            }


        }

        return true;
    }
}
