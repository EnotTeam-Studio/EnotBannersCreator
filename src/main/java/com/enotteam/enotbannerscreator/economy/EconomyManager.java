package com.enotteam.enotbannerscreator.economy;

import org.bukkit.entity.Player;

public interface EconomyManager {

    boolean has(Player player, int amount);
    void take(Player player, int amount);

}
