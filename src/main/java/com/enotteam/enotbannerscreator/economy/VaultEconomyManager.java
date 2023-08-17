package com.enotteam.enotbannerscreator.economy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VaultEconomyManager implements EconomyManager {

    Economy economy;

    @Override
    public boolean has(Player player, int amount) {
        return economy.has(player, amount);
    }

    @Override
    public void take(Player player, int amount) {
        economy.withdrawPlayer(player, amount);
    }

}
