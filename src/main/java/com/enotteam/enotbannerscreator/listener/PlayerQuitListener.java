package com.enotteam.enotbannerscreator.listener;

import com.enotteam.enotbannerscreator.database.cache.PlayerCache;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlayerQuitListener implements RegistrableListener {

    PlayerCache playerCache;

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        playerCache.saveAndUnload(event.getPlayer().getName());
    }

}
