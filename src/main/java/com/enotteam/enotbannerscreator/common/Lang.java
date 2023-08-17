package com.enotteam.enotbannerscreator.common;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Lang {

    private static JavaPlugin javaPlugin;
    private static File file;
    private static YamlConfiguration configuration;

    public static void initialize(JavaPlugin pluginInstance, String lang) {
        javaPlugin = pluginInstance;
        createFile(lang);
    }

    private static void createFile(String fileName) {

        file = new File(javaPlugin.getDataFolder(), "lang/" + fileName + ".yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            javaPlugin.saveResource("lang/" + fileName + ".yml", false);
        }

        configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

    }

    public static YamlConfiguration get() {
        return configuration;
    }

    public static void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
