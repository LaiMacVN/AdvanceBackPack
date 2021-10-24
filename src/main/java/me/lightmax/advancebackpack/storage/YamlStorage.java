package me.lightmax.advancebackpack.storage;

import me.lightmax.advancebackpack.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.UUID;

public class YamlStorage {

    Main plugin;

    public YamlStorage(Main plugin) {
        this.plugin = plugin;
    }

    public void createStorage(String name, String folder) {
        if (folder == null) {
            File file = new File(Bukkit.getServer().getPluginManager().getPlugin("AdvanceBackPack").getDataFolder().getName(), name);
            if (!name.contains(".yml")) {
                file.mkdir();
                return;
            }
            if (!file.exists()) {
                this.plugin.saveResource(file.getName(), true);
            }
            return;
        }
        if (!folder.contains("/")) return;
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("AdvanceBackPack").getDataFolder().getName() + folder, name);
        if (!name.contains(".yml")) {
            file.mkdir();
            return;
        }
        if (!file.exists()) {
            this.plugin.saveResource(file.getName(), true);
        }
    }

    public void reloadConfig(String name) {
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("AdvanceBackPack").getDataFolder(), name);
        if(!name.contains(".yml")) return;
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        InputStream inputStream = this.plugin.getResource(file.getName());
        if(inputStream != null) {
            YamlConfiguration configuration1 = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream));
            configuration.setDefaults(configuration1);
        }
    }


    public FileConfiguration getConfig(String name, String folder) {
        if (folder == null) {
            File file = new File(Bukkit.getServer().getPluginManager().getPlugin("AdvanceBackPack").getDataFolder().getName());
            File[] fileList = file.listFiles();
            if (fileList == null) return null;
            for (File listFile : fileList) {
                if (!listFile.isFile()) return null;
                if (listFile.getName().equalsIgnoreCase(name)) {
                    return YamlConfiguration.loadConfiguration(listFile);
                }
            }
            return null;
        }
        if (!folder.contains("/")) return null;
        File file = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("AdvanceBackPack")).getDataFolder().getName() + folder);
        File[] fileList = file.listFiles();
        if (fileList == null) return null;
        for (File listFile : fileList) {
            if (!listFile.isFile()) return null;
            if (listFile.getName().equalsIgnoreCase(name)) {
                return YamlConfiguration.loadConfiguration(listFile);
            }
        }
        return null;
    }

    public boolean isExistPlayer(UUID uuid, String folder) {
        if (!folder.contains("/")) return true;
        File file = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("AdvanceBackPack")).getDataFolder().getName() + folder);
        File[] fileList = file.listFiles();
        if(fileList != null) {
            for (File listFile : fileList) {
                if (!listFile.isFile()) return true;
                if (listFile.getName().equalsIgnoreCase(uuid.toString() + ".yml")) {
                    return false;
                }
            }
        }
        return true;
    }

    public FileConfiguration getConfigFile(String name) {
        File file = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("AdvanceBackPack")).getDataFolder(), name);
        if(!name.contains(".yml")) return null;
        return YamlConfiguration.loadConfiguration(file);
    }
}
