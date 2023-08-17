package com.enotteam.enotbannerscreator.economy;

import org.bukkit.entity.Player;

public class StubEconomyManager implements EconomyManager {

    @Override
    public boolean has(Player player, int amount) {
        return false;
    }

    @Override
    public void take(Player player, int amount) {

    }

}
