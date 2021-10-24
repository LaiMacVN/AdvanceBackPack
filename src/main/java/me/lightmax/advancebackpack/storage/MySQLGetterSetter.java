package me.lightmax.advancebackpack.storage;

import me.lightmax.advancebackpack.ItemStackSerializer;
import me.lightmax.advancebackpack.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class MySQLGetterSetter {

    Main plugin;

    public MySQLGetterSetter(Main plugin) {
        this.plugin = plugin;
    }

    public Logger logger;

    public void createTable() {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS `" + plugin.tableName
                    + "` (InventoryID INT(100),NAME VARCHAR(100),UUID VARCHAR(100),InventoryContent LONGTEXT,PRIMARY KEY (InventoryID))");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("ERROR AT CREATING TABLE!");
        }
    }

    public boolean isPlayerExist(int id, UUID uuid) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT * FROM `" + plugin.tableName + "` WHERE InventoryID= ? and UUID= ?");
            ps.setInt(1, id);
            ps.setString(2, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("ERROR AT CHECK IF PLAYER IS EXIST");
        }
        return false;
    }

    public boolean isPlayerExistWithThatName(int id, String name) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT * FROM `" + plugin.tableName + "` WHERE InventoryID=? and NAME= ?");
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

    public void createPlayer(int id, Player player) {
        try {
            UUID uuid = player.getUniqueId();
            if (!isPlayerExist(id, player.getUniqueId())) {
                PreparedStatement ps = plugin.sql.getConnection().prepareStatement("INSERT INTO `" + plugin.tableName + "` (InventoryID,NAME,UUID) VALUES (?,?,?)");
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

    public void setInventoryContent(int id, UUID uuid, ItemStack[] itemStacks) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("UPDATE `" + plugin.tableName + "` SET InventoryContent=? WHERE InventoryID= ? and UUID= ?");
            ps.setString(1, ItemStackSerializer.convertitemStackArrayToBase64(itemStacks));
            ps.setInt(2, id);
            ps.setString(3, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("ERROR AT SET PLAYER INVENTORY!");
        }
    }

    public String getInventoryContent(int id, UUID uuid) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT InventoryContent FROM `" + plugin.tableName + "` WHERE InventoryID= ? and UUID= ?");
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

    public void deletePlayerVault(int id, UUID uuid) {
        try {
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("DELETE FROM `" + plugin.tableName + "` WHERE InventoryID=? and UUID=?");
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
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("DELETE FROM `" + plugin.tableName + "` WHERE InventoryID=? and NAME=?");
            ps.setInt(1, id);
            ps.setString(2, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            logger.info("ERROR AT DELETING PLAYER BACKPACK");
        }
    }
}
