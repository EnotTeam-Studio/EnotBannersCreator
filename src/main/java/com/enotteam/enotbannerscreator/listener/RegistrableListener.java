package com.enotteam.enotbannerscreator.listener;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public interface RegistrableListener extends Listener {

    default void register(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    default void unregister() {
        HandlerList.unregisterAll(this);
    }

}
