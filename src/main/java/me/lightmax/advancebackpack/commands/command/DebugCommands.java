package me.lightmax.advancebackpack.commands.command;

import me.lightmax.advancebackpack.ItemStackSerializer;
import me.lightmax.advancebackpack.Main;
import me.lightmax.advancebackpack.commands.SubCommands;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.UUID;

public class DebugCommands extends SubCommands {

    Main plugin;

    public DebugCommands(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "debug";
    }

    @Override
    public String getDesc() {
        return "Debug plugin, testing new feature";
    }

    @Override
    public String getSyntax() {
        return "/backpack debug";
    }

    @Override
    public String getPermission() {
        return "default"; //"AdvanceBackPack.debug";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length > 1) {
            switch (args[1]) {
                case "1":
                    ItemStack[] itemStacks = player.getInventory().getContents();
                    player.sendMessage(ItemStackSerializer.convertitemStackArrayToBase64(itemStacks));
                    break;
                case "2":
                    try {
                        plugin.sql.disconnect();
                        player.sendMessage(plugin.utils.color("&aĐã disconnet MySQL thành công!"));
                        plugin.logger.info("Đã disconnect MySQL thành công!");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    plugin.getPerms().playerAdd(null, player, args[2]);
                    player.sendMessage("Added");
                    break;
                case "4":
                    if (plugin.utils.isInt(args[2])) {
                        EconomyResponse economy = plugin.getEco().depositPlayer(player, Integer.parseInt(args[2]));
                        if (economy.transactionSuccess()) {
                            player.sendMessage(plugin.utils.color("&aĐã thành công thêm " + economy.amount + " cho bạn!"));
                        } else {
                            player.sendMessage(plugin.utils.color("&cĐã xảy ra lỗi khi thêm tiền! Hãy thử lại lần nữa!"));
                        }
                    }
                    break;
                case "5":
                    String name = args[2];
                    UUID uuid = UUID.nameUUIDFromBytes(name.getBytes());
                    player.sendMessage(uuid.toString());
                    break;
                case "6":
                    player.sendMessage(plugin.utils.color("&bTiền hiện tại của bạn: " + plugin.getPlayerMoney(player)));
                    break;
                case "7":
                    plugin.getPerms().playerRemove(player, args[2]);
                    break;
                default:
                    player.sendMessage(plugin.utils.color("&e&l&m----------------------------------"));
                    player.sendMessage(plugin.utils.color("&b/backpack debug 1&7: Serialize all your inventory content\n" +
                            "&b/backpack debug 2&7: Force disconnect database &c(Only use if it has purpose) &c&lUSE IT ON YOUR OWN RISK\n" +
                            "&b/backpack debug 3 [permission]&7: Add your permission!!\n" +
                            "&b/backpack debug 4 [amount]&7: Give yourself the amount of money without using essentials or other economy plugin\n" +
                            "&b/backpack debug 5 [name]&7: Migrate name into UUID &c(this is just a test, it might not be migrate right!)\n" +
                            "&b/backpack debug 6&7:Show your current money!\n" +
                            "&b/backpack debug 7 [permission]&7: Remove your Permission!"));
                    player.sendMessage(plugin.utils.color("&e&l&m----------------------------------"));
                    break;
            }
        }
        if (args.length == 1) {
            player.sendMessage(plugin.utils.color("&e&l&m----------------------------------"));
            player.sendMessage(plugin.utils.color("&b/backpack debug 1&7: Serialize all your inventory content\n" +
                    "&b/backpack debug 2&7: Force disconnect database &c(Only use if it has purpose) &c&lUSE IT ON YOUR OWN RISK\n" +
                    "&b/backpack debug 3&7: Secret command!!!\n" +
                    "&b/backpack debug 4 [amount]&7: Give yourself the amount of money without using essentials or other economy plugin\n" +
                    "&b/backpack debug 5 [name]&7: Migrate name into UUID &c(this is just a test, it might not be migrate right!)\n" +
                    "&b/backpack debug 6&7:Show your current money!\n" +
                    "&b/backpack debug 7 [permission]&7:Remove your Permission!"));
            player.sendMessage(plugin.utils.color("&e&l&m----------------------------------"));
        }
    }
}
