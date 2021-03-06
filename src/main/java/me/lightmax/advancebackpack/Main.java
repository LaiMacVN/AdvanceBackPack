package me.lightmax.advancebackpack;

import me.lightmax.advancebackpack.commands.CommandManager;
import me.lightmax.advancebackpack.cooldown.ClickCooldown;
import me.lightmax.advancebackpack.cooldown.CommandCooldowns;
import me.lightmax.advancebackpack.listener.CloseInventoryEvent;
import me.lightmax.advancebackpack.listener.MenuListener;
import me.lightmax.advancebackpack.menus.PlayerMenuUtility;
import me.lightmax.advancebackpack.storage.MySQLUtils;
import me.lightmax.advancebackpack.storage.MySQLSetUp;
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
    public MySQLUtils data;
    public HashMap<UUID, PlayerMenuUtility> playerMenuUtilityHashMap = new HashMap<>();
    public Economy eco;
    public Permission perms;
    public ClickCooldown Clickcooldown;
    public CommandCooldowns commandCooldowns;

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
        this.data = new MySQLUtils(this);
        this.Clickcooldown = new ClickCooldown();
        this.commandCooldowns = new CommandCooldowns();


        if (!this.isEnableMySQl && !this.isEnableYAMLStorage) {
            logger.info("T???T PLUGIN B???I V?? L??U TR??? B???NG MYSQL V?? YAML ?????U B??? T???T! V??o config v?? ch???nh l???i! ");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }


        logger.info("-----------------------------------------------");
        logger.info("AdvanceBackPack ???????c l??m b???i LightMaxVN");
        logger.info("Version: 1.0.0");
        logger.info("Server version: " + Bukkit.getServer().getVersion() + "!");
        logger.info("-----------------------------------------------");


        if (!setupEconomy()) {
            logger.info("Kh??ng t??m th???y vault, Plugin s??? kh??ng ho???t ?????ng! H??y t???i vaults");
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
                logger.info("Kh??ng th??? k???t n???i v???i database!");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
            if (this.sql.isConnected()) {
                logger.info("???? k???t n???i database th??nh c??ng t???n " + this.utils.getTime(time1) + "ms");
                this.data.createTable();
                registerEventAndCommand();
            }
            return;
        }

        if (this.isEnableYAMLStorage) {
            //owo
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
        logger.info("T???t Plugin!");
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
                player.sendMessage(this.utils.color("&a???? n??ng c???p th??nh c??ng!"));
            } else {
                player.sendMessage(this.utils.color("&cB???n ???? n??ng c???p c???p ????? n??y r???i!"));
                return;
            }
        } else {
            player.sendMessage(this.utils.color("&cB???n kh??ng c?? ????? ti???n ????? n??ng c???p!"));
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
            player.sendMessage(this.utils.color("&c???? c?? l???i x???y ra! H??y th??? l???i l???n n???a!"));
        }
    }

    public Permission getPerms() {
        return perms;
    }

    public void registerEventAndCommand() {
        double time = System.currentTimeMillis();
        logger.info("??ang ????ng k?? c??c l???nh...");
        Objects.requireNonNull(getCommand("backpack")).setExecutor(new CommandManager(this));
        Objects.requireNonNull(getCommand("backpack")).setTabCompleter(new CommandManager(this));
        logger.info("????ng k?? c??c l???nh th??nh c??ng t???n " + this.utils.getTime(time) + "ms");
        double time3 = System.currentTimeMillis();
        logger.info("??ang ????ng k?? c??c event...");
        getServer().getPluginManager().registerEvents(new CloseInventoryEvent(this), this);
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);
        logger.info("????ng k?? c??c event th??nh c??ng t???n " + this.utils.getTime(time3) + "ms");
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
