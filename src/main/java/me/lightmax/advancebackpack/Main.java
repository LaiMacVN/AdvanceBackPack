package me.lightmax.advancebackpack;

import me.lightmax.advancebackpack.GuiManager.MainGUI;
import me.lightmax.advancebackpack.commands.CommandManager;
import me.lightmax.advancebackpack.listener.CloseInventoryEvent;
import me.lightmax.advancebackpack.storage.MySQLSetUp;
import me.lightmax.advancebackpack.storage.MySQLSetterGetter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    public Logger logger = Bukkit.getLogger();

    public Utils utils;

    public MySQLSetUp sql;
    public MySQLSetterGetter data;
    public MainGUI mainGUI;

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
        this.mainGUI = new MainGUI(this);
        this.utils = new Utils();
        this.sql = new MySQLSetUp(this);
        this.data = new MySQLSetterGetter(this);

        logger.info("------------------------------------");
        logger.info("AdvanceBackPack được làm bởi LightMaxVN");
        logger.info("Version: 1.0.0");
        logger.info("Server version: " + Bukkit.getServer().getVersion() + "!");
        logger.info("-------------------------------------");
        double time1 = System.currentTimeMillis();
        try {
            this.sql.connect();
        } catch (ClassNotFoundException | SQLException e) {
            logger.info("CANNOT CONNECTED TO DATABASE! DISABLE PLUGIN!");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        if(this.sql.isConnected()) {
            logger.info("Đã kết nối database thành công tốn " + this.utils.getTime(time1) + "ms");
            this.data.createTable();
            double time = System.currentTimeMillis();
            logger.info("Đang đăng ký các lệnh...");
            Objects.requireNonNull(getCommand("backpack")).setExecutor(new CommandManager(this));
            logger.info("Đăng ký các lệnh thành công tốn " + this.utils.getTime(time) + "ms");
            double time3 = System.currentTimeMillis();
            logger.info("Đang đăng ký các event...");
            getServer().getPluginManager().registerEvents(new CloseInventoryEvent(this), this);
            logger.info("Đăng ký các event thành công tốn " + this.utils.getTime(time3) + "ms");
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            sql.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
