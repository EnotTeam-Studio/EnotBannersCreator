package com.enotteam.enotbannerscreator.banner.rotating;

import com.enotteam.enotbannerscreator.common.Rotation;
import com.enotteam.enotbannerscreator.common.StringLocation;
import org.bukkit.Location;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class BannerRotateManager {

    JavaPlugin plugin;
    HashMap<String, String> playerRotatableBanners = new HashMap<>();

    String bypassPermission;
    HashMap<String, Integer> maxAmountPermissions = new HashMap<>();

    public BannerRotateManager(JavaPlugin plugin, ConfigurationSection section) {

        this.plugin = plugin;
        RotatableData.initialize(plugin);

        Configuration data = RotatableData.get();

        if (data.getConfigurationSection("data") != null) {
            data.getConfigurationSection("data").getKeys(false).forEach(location -> {
                playerRotatableBanners.put(location, data.getString("data." + location));
            });
        }

        startRotating();

        this.bypassPermission = section.getString("permission.bypass-amount");
        section.getConfigurationSection("permission.max-amount").getKeys(false).forEach(permission -> {
            maxAmountPermissions.put(permission, section.getInt("permission.max-amount." + permission));
        });

    }

    public void startRotating() {
        playerRotatableBanners.forEach((location, playerName) -> {
                new RotatableBanner(
                        StringLocation.fromString(location).getBlock().getState(),
                        this).startRotating(plugin);
        });
    }

    public void add(String ownerPlayerName, Location location) {
        String key = StringLocation.toString(location, false);
        RotatableData.get().set("data." + key, ownerPlayerName);
        RotatableData.save();
        playerRotatableBanners.put(key, ownerPlayerName);
        new RotatableBanner(location.getBlock().getState(), this).startRotating(plugin);
    }

    public void remove(Location location) {
        String key = StringLocation.toString(location, false);
        RotatableData.get().set("data." + key, null);
        RotatableData.save();
        playerRotatableBanners.remove(key);
    }

    public boolean canCreateNewRotatableBanner(Player player) {

        if (player.hasPermission(bypassPermission)) return true;

        int maxAmount = 0;
        for (Map.Entry<String, Integer> entry : maxAmountPermissions.entrySet()) {
            if (player.hasPermission(entry.getKey())) maxAmount = Math.max(maxAmount, entry.getValue());
        }
        if (maxAmount == 0) return false;

        String playerName = player.getName();

        long currentBanners = playerRotatableBanners.values().stream()
                .filter(name -> name.equals(playerName))
                .count();

        return currentBanners >= maxAmount;

    }

}
