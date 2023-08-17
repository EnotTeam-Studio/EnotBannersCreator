package com.enotteam.enotbannerscreator.banner.rotating;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class RotatableData {

    private static final String FILE_NAME = "rotatable_banners_data.yml";

    private static JavaPlugin javaPlugin;
    private static File file;
    private static YamlConfiguration configuration;

    public static void initialize(JavaPlugin pluginInstance) {
        javaPlugin = pluginInstance;
        createFile();
    }

    private static void createFile() {

        file = new File(javaPlugin.getDataFolder(), FILE_NAME);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            javaPlugin.saveResource(FILE_NAME, false);
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
