package me.lightmax.advancebackpack.storage;

import me.lightmax.advancebackpack.ItemStackSerializer;
import me.lightmax.advancebackpack.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class MySQLUtils {

    Main plugin;

    public Logger logger;

    protected String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `%s` (InventoryID INT(100),NAME VARCHAR(100),UUID VARCHAR(100),InventoryContent LONGTEXT,PRIMARY KEY (InventoryID))";
    protected String INSERT_VALUE = "INSERT INTO `%s` (InventoryID,NAME,UUID) VALUES (?,?,?)";
    protected String CHECK_PLAYER =  "SELECT InventoryContent FROM `%s` WHERE InventoryID= ? and UUID= ?";
    protected String CHECK_PLAYER_NAME = "SELECT InventoryContent FROM `%s` WHERE InventoryID= ? and NAME= ?";
    protected String REMOVE_VALUE = "DELETE FROM `%s` WHERE InventoryID=? and UUID=?";
    protected String REMOVE_VALUE_PLAYER_NAME = "DELETE FROM `%s` WHERE InventoryID=? and NAME=?";
    protected String UPDATE_PLAYER_INVENTORY = "UPDATE `%s` SET InventoryContent=? WHERE InventoryID= ? and UUID= ?";
    protected String GET_PLAYER_INVENTORY_CONTENT = "SELECT InventoryContent FROM `%s` WHERE InventoryID= ? and UUID= ?";

    public MySQLUtils(@NotNull Main plugin) {
        this.plugin = plugin;
        CREATE_TABLE = String.format(CREATE_TABLE, plugin.tableName);
        INSERT_VALUE = String.format(INSERT_VALUE, plugin.tableName);
        CHECK_PLAYER = String.format(CHECK_PLAYER, plugin.tableName);
        CHECK_PLAYER_NAME = String.format(CHECK_PLAYER_NAME, plugin.tableName);
        REMOVE_VALUE = String.format(REMOVE_VALUE, plugin.tableName);
        REMOVE_VALUE_PLAYER_NAME = String.format(REMOVE_VALUE_PLAYER_NAME, plugin.tableName);
        UPDATE_PLAYER_INVENTORY = String.format(UPDATE_PLAYER_INVENTORY, plugin.tableName);
        GET_PLAYER_INVENTORY_CONTENT = String.format(GET_PLAYER_INVENTORY_CONTENT, plugin.tableName);
    }

    public void createTable() {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(CREATE_TABLE);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("ERROR AT CREATING TABLE!");
        }
    }

    public boolean isPlayerExist(int id, @NotNull UUID uuid) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(CHECK_PLAYER);
            ps.setInt(1, id);
            ps.setString(2, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            return !resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("ERROR AT CHECK IF PLAYER IS EXIST");
        }
        return true;
    }

    public boolean isPlayerExistWithThatName(int id, String name) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(CHECK_PLAYER_NAME);
            ps.setInt(1, id);
            ps.setString(2, name);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("ERROR AT CHECK THE NAME OF THE PLAYERS IS EXISTS");
        }
        return false;
    }


    public void createPlayer(int id, @NotNull Player player) {
        try {
            UUID uuid = player.getUniqueId();
            if (isPlayerExist(id, player.getUniqueId())) {
                PreparedStatement ps = plugin.sql.getConnection().prepareStatement(INSERT_VALUE);
                ps.setInt(1, id);
                ps.setString(2, player.getName());
                ps.setString(3, uuid.toString());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("ERROR AT CREATING PLAYERS!");
        }
    }

    public void setInventoryContent(int id, @NotNull UUID uuid, ItemStack[] itemStacks) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(UPDATE_PLAYER_INVENTORY);
            ps.setString(1, ItemStackSerializer.convertitemStackArrayToBase64(itemStacks));
            ps.setInt(2, id);
            ps.setString(3, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("ERROR AT SET PLAYER INVENTORY!");
        }
    }

    public String getInventoryContent(int id, @NotNull UUID uuid) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(GET_PLAYER_INVENTORY_CONTENT);
            ps.setInt(1, id);
            ps.setString(2, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("InventoryContent");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("ERROR AT GETTING PLAYER INVENTORY CONTENT");
        }
        return null;
    }

    public void deletePlayerVault(int id, @NotNull UUID uuid) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(REMOVE_VALUE);
            ps.setInt(1, id);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("ERROR AT DELETING PLAYER BACKPACK");
        }
    }

    public void deletePlayerWithTheNameVault(int id, String uuid) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement(REMOVE_VALUE_PLAYER_NAME);
            ps.setInt(1, id);
            ps.setString(2, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("ERROR AT DELETING PLAYER BACKPACK");
        }
    }
}
