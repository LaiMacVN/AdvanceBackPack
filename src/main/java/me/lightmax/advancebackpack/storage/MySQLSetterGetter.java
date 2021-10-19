package me.lightmax.advancebackpack.storage;

import me.lightmax.advancebackpack.ItemStackSerializer;
import me.lightmax.advancebackpack.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


public class MySQLSetterGetter {

    Main plugin;

    public MySQLSetterGetter(Main plugin) {
        this.plugin = plugin;
    }
    /*
    Create table if not exist
    PlayerName (VARCHAR 36)
    PlayerUUID (VARCHAR 36)
    Inventory ID (bigInt)
    INVENTORY CONTENT (LONGTEXT)
     */

    // CREATE TABLE

    public void createTable() {
        PreparedStatement ps;
        try{
            ps = plugin.sql.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + plugin.tableName
                    + " (InventoryID INT(100),NAME VARCHAR(100),UUID VARCHAR(100),InventoryCONTENT LONGTEXT,PRIMARY KEY (InventoryID))");
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CREATE PLAYERS IN MYSQL TABLE

    public void createPlayer(int id, Player player) {
        try{
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT * FROM " + plugin.tableName + " WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            if(!isExist(id, uuid)) {
                PreparedStatement ps2 = plugin.sql.getConnection().prepareStatement("INSERT INTO " + plugin.tableName
                + "(InventoryID,NAME,UUID) VALUES(?,?,?)");
                ps2.setInt(1, id);
                ps2.setString(2, player.getName());
                ps2.setString(3, uuid.toString());
                ps2.executeUpdate();

                return;
            }
        }catch (SQLException e) {
            //owo
        }
    }

    // CHECK IF PLAYERS IS EXISTS

    public boolean isExist(int id, UUID uuid) {
        try{
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT * FROM " + plugin.tableName + "WHERE InventoryID=? WHERE UUID=?");
            ps.setInt(1, id);
            ps.setString(2, uuid.toString());

            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // SET THE PLAYERS CONTENT

    public void setInventoryContent(int id, UUID uuid, ItemStack[] itemStacks) {
        try{
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("UPDATE " + plugin.tableName + " SET InventoryCONTENT=? WHERE InventoryID=? WHERE UUID=?");
            ps.setString(1, ItemStackSerializer.convertitemStackArrayToBase64(itemStacks));
            ps.setInt(2, id);
            ps.setString(3, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // GET PLAYER ITEMS CONTENT


    public String getInventoryContent(int id, UUID uuid) {
        try{
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("SELECT InventoryCONTENT FROM " + plugin.tableName + " WHERE InventoryID=?  WHERE UUID=?");
            ps.setInt(1, id);
            ps.setString(2, uuid.toString());
            ResultSet rs = ps.executeQuery();
            String itemStack;
            if (rs.next()) {
                itemStack = rs.getString("InventoryCONTENT");
                return itemStack;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // DELETE PLAYERS DATA IN MYSQL (NOT DONE YET)

    public void emptyTable() {
        try{
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("TRUNCATE " + plugin.database);
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(int id, UUID uuid) {
        try{
            PreparedStatement ps = plugin.sql.getConnection().prepareStatement("DELETE FROM " + plugin.database + " WHERE InventoryID=? and UUID=?");
            ps.setInt(1, id);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
