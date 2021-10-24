package me.lightmax.advancebackpack;

import me.lightmax.advancebackpack.commands.CommandManager;
import me.lightmax.advancebackpack.cooldown.ClickCooldown;
import me.lightmax.advancebackpack.cooldown.CommandCooldowns;
import me.lightmax.advancebackpack.listener.CloseInventoryEvent;
import me.lightmax.advancebackpack.listener.MenuListener;
import me.lightmax.advancebackpack.menus.PlayerMenuUtility;
import me.lightmax.advancebackpack.storage.MySQLGetterSetter;
import me.lightmax.advancebackpack.storage.MySQLSetUp;
import me.lightmax.advancebackpack.storage.YamlStorage;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    public Logger logger = Bukkit.getLogger();

    public Utils utils;

    public MySQLSetUp sql;
    public MySQLGetterSetter data;
    public HashMap<UUID, PlayerMenuUtility> playerMenuUtilityHashMap = new HashMap<>();
    public Economy eco;
    public Permission perms;
    public ClickCooldown Clickcooldown;
    public CommandCooldowns commandCooldowns;
    public YamlStorage yamlStorage;

    public Boolean isEnableMySQl = this.getConfig().getBoolean("mysql.enable");
    public Boolean isEnableYAMLStorage = this.getConfig().getBoolean("EnableYAMLStorage");
    public String tableName = this.getConfig().getString("mysql.tableName");
    public String username = this.getConfig().getString("mysql.username");
    public String password = this.getConfig().getString("mysql.password");
    public String database = this.getConfig().getString("mysql.database");
    public String host = this.getConfig().getString("mysql.host");
    public String port = this.getConfig().getString("mysql.port");
    public Boolean useSSL = this.getConfig().getBoolean("mysql.useSSL");

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.saveDefaultConfig();
        this.utils = new Utils(this);
        this.sql = new MySQLSetUp(this);
        this.data = new MySQLGetterSetter(this);
        this.Clickcooldown = new ClickCooldown();
        this.commandCooldowns = new CommandCooldowns();
        this.yamlStorage = new YamlStorage(this);

        this.yamlStorage.createStorage("message.yml", null);

        if (!this.isEnableMySQl && !this.isEnableYAMLStorage) {
            logger.info("TẮT PLUGIN BỞI VÌ LƯU TRỮ BẰNG MYSQL VÀ YAML ĐỀU BỊ TẮT! Vào config và chỉnh lại! ");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }


        logger.info("-----------------------------------------------");
        logger.info("AdvanceBackPack được làm bởi LightMaxVN");
        logger.info("Version: 1.0.0");
        logger.info("Server version: " + Bukkit.getServer().getVersion() + "!");
        logger.info("-----------------------------------------------");


        if (!setupEconomy()) {
            logger.info("Không tìm thấy vault, Plugin sẽ không hoạt động! Hãy tải vaults");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        logger.info("FOUND VAULTS!!!");
        setupPermissions();


        double time1 = System.currentTimeMillis();
        if (this.isEnableMySQl) {
            try {
                this.sql.connect();
            } catch (ClassNotFoundException | SQLException e) {
                logger.info("Không thể kết nối với database!");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            if (this.sql.isConnected()) {
                logger.info("Đã kết nối database thành công tốn " + this.utils.getTime(time1) + "ms");
                this.data.createTable();
                registerEventAndCommand();
            }
            return;
        }

        if (this.isEnableYAMLStorage) {
            this.yamlStorage.createStorage("data", null);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (this.isEnableMySQl) {
            try {
                sql.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        logger.info("Tắt Plugin!");
    }

    public Economy getEco() {
        return this.eco;
    }

    public boolean checkPlayerMoney(Player player, int amount) {
        return this.getPlayerMoney(player) >= amount;
    }

    public void buyUpgradeMethod(Player player, int amount, int upgradeSize) {
        if (this.checkPlayerMoney(player, amount)) {
            if (this.utils.checkPlayerBackPackAmount(9, "AdvanceBackPack.use.", player) < upgradeSize) {
                this.removePlayerMoney(player, amount);
                this.getPerms().playerAdd(player, "AdvanceBackPack.use." + upgradeSize);
                player.sendMessage(this.utils.color("&aĐã nâng cấp thành công!"));
            } else {
                player.sendMessage(this.utils.color("&cBạn đã nâng cấp cấp độ này rồi!"));
                return;
            }
        } else {
            player.sendMessage(this.utils.color("&cBạn không có đủ tiền để nâng cấp!"));
            return;
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    public double getPlayerMoney(Player player) {
        return this.getEco().getBalance(player);
    }

    public void removePlayerMoney(Player player, double amount) {
        EconomyResponse r = getEco().withdrawPlayer(player, amount);
        if (!r.transactionSuccess()) {
            player.sendMessage(this.utils.color("&cĐã có lỗi xảy ra! Hãy thử lại lần nữa!"));
        }
    }

    public Permission getPerms() {
        return perms;
    }

    public void registerEventAndCommand() {
        double time = System.currentTimeMillis();
        logger.info("Đang đăng ký các lệnh...");
        Objects.requireNonNull(getCommand("backpack")).setExecutor(new CommandManager(this));
        Objects.requireNonNull(getCommand("backpack")).setTabCompleter(new CommandManager(this));
        logger.info("Đăng ký các lệnh thành công tốn " + this.utils.getTime(time) + "ms");
        double time3 = System.currentTimeMillis();
        logger.info("Đang đăng ký các event...");
        getServer().getPluginManager().registerEvents(new CloseInventoryEvent(this), this);
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
        logger.info("Đăng ký các event thành công tốn " + this.utils.getTime(time3) + "ms");
    }

    public PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityHashMap.containsKey(p.getUniqueId()))) {

            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityHashMap.put(p.getUniqueId(), playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityHashMap.get(p.getUniqueId());
        }
    }


}
