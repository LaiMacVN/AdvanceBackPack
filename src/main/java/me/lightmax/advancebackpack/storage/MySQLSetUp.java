package me.lightmax.advancebackpack.storage;

import me.lightmax.advancebackpack.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLSetUp {

    Main plugin;

    public MySQLSetUp(Main plugin) {
        this.plugin = plugin;
    }

    private Connection connection;

    public boolean isConnected() {
        return connection != null;
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if (!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" +
                            plugin.host + ":" + plugin.port + "/" + plugin.database + "?useSSL=" + plugin.useSSL,
                    plugin.username, plugin.password);
        }
    }

    public void disconnect() throws SQLException {
        if (isConnected()) {
            connection.close();
        }
    }

    public Connection getConnection() {
        return connection;
    }


}
