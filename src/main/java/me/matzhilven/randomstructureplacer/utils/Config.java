package me.matzhilven.randomstructureplacer.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Config {

    private final Plugin main;

    private final String name;
    private final File file;

    private FileConfiguration config;

    public Config(Plugin main, String name) {
        this.main = main;
        this.name = name;
        this.file = new File(main.getDataFolder(), name);

        setup();
    }

    private void setup() {
        if (!file.exists()) {
            file.getParentFile().mkdir();
            main.saveResource(file.getName(), false);
        }

        config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            main.getServer().getConsoleSender().sendMessage("Error saving " + name);
            e.printStackTrace();
        }
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public List<Integer> getIntegerList(String path) {
        return config.getIntegerList(path);
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reload() {
        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
