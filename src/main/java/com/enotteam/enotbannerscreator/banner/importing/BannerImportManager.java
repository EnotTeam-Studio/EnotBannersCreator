package com.enotteam.enotbannerscreator.banner.importing;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BannerImportManager {

    JavaPlugin plugin;
    HashMap<Player, Integer> playersInImportWaiting = new HashMap<>();

    public void addToImportWaiting(Player player) {

        removeFromImportWaiting(player);

        int task = plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            playersInImportWaiting.remove(player);
        }, 200L).getTaskId();

        playersInImportWaiting.put(player, task);

    }

    public void removeFromImportWaiting(Player player) {
        if (playersInImportWaiting.containsKey(player)) {
            plugin.getServer().getScheduler().cancelTask(playersInImportWaiting.get(player));
            playersInImportWaiting.remove(player);
        }
    }

    public boolean isInImportWaiting(Player player) {
        return playersInImportWaiting.containsKey(player);
    }

}
