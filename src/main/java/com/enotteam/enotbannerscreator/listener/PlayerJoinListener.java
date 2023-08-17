package com.enotteam.enotbannerscreator.listener;

import com.enotteam.enotbannerscreator.database.cache.PlayerCache;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerJoinListener implements RegistrableListener {

    PlayerCache playerCache;

    @EventHandler(ignoreCancelled = true)
    private void onJoin(PlayerJoinEvent event) {
        playerCache.load(event.getPlayer().getName());
    }

}
