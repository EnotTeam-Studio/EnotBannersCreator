package com.enotteam.enotbannerscreator.economy;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.entity.Player;

public class PlayerPointsEconomyManager implements EconomyManager {

    PlayerPointsAPI api;

    public PlayerPointsEconomyManager() {
        api = PlayerPoints.getInstance().getAPI();
    }

    @Override
    public boolean has(Player player, int amount) {
        return api.look(player.getUniqueId()) >= amount;
    }

    @Override
    public void take(Player player, int amount) {
        api.take(player.getUniqueId(), amount);
    }

}
