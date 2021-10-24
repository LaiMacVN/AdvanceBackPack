package me.lightmax.advancebackpack.commands.command;

import me.lightmax.advancebackpack.Main;
import me.lightmax.advancebackpack.commands.SubCommands;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class DeleteBackPackCommand extends SubCommands {

    Main plugin;

    public DeleteBackPackCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public String getDesc() {
        return "Xóa backpack của người chơi khác";
    }

    @Override
    public String getSyntax() {
        return "/backpack delete <player> [VaultID]";
    }

    @Override
    public String getPermission() {
        return "AdvanceBackPack.delete";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length <= 2) {
            player.sendMessage(plugin.utils.color("&eSử dụng như: &e" + getSyntax()));
        }
        if (args.length > 2) {
            int vaultId = 0;
            if (plugin.utils.isInt(args[2])) {
                vaultId = Integer.parseInt(args[2]);
                if (vaultId < 0 || vaultId > 9) {
                    player.sendMessage(plugin.utils.color("&cVaultId cần lớn hơn 0 và nhỏ hơn 10"));
                    return;
                }
            } else {
                player.sendMessage(plugin.utils.color("&cVaultId cần là một số dương!"));
            }
            Player player1 = Bukkit.getPlayerExact(args[1]);
            if (player1 == null) {
                if (!plugin.data.isPlayerExistWithThatName(vaultId, args[1])) {
                    player.sendMessage(plugin.utils.color("&cVaultID của người chơi đó không hề tồn tại! Có thể họ đã chưa bỏ đồ vào hoặc chưa có trong dữ liệu của database!"));
                    return;
                }

                plugin.data.deletePlayerWithTheNameVault(vaultId, args[1]);
                player.sendMessage(plugin.utils.color("&cĐã delete backpack &b" + vaultId + "&c của &b" + args[1]));
                return;
            }

            if (!plugin.data.isPlayerExist(vaultId, player1.getUniqueId())) {
                player.sendMessage(plugin.utils.color("&cVaultID của người chơi đó không hề tồn tại! Có thể họ đã chưa bỏ đồ vào hoặc chưa có trong dữ liệu của database!"));
                return;
            }
            plugin.data.deletePlayerVault(vaultId, player1.getUniqueId());
            player.sendMessage(plugin.utils.color("&cĐã delete backpack &b" + vaultId + "&c của &b" + player1.getName()));
        }
    }
}
