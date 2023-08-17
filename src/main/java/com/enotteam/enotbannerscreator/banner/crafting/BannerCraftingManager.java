package com.enotteam.enotbannerscreator.banner.crafting;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BannerCraftingManager {

    HashMap<String, Integer> materialsCost = new HashMap<>();
    @Getter
    BannerCraftingType type;
    @Getter
    boolean needPermission;
    @Getter
    String permission;

    public BannerCraftingManager(ConfigurationSection section) {

        this.type = BannerCraftingType.valueOf(section.getString("mode", "FREE"));
        this.needPermission = section.getBoolean("permission.need", true);
        this.permission = section.getString("permission.value", "enotbanners.crafting");

        section.getConfigurationSection("materials-cost").getKeys(false).forEach(material -> {
            materialsCost.put(material, section.getInt("materials-cost." + material));
        });

    }

    public int getCost(List<ItemStack> resources) {
        int cost = 0;
        for (ItemStack resource : resources) {
            int materialCost = materialsCost.getOrDefault(resource.getType() + ":" + resource.getDurability(), 0);
            cost += materialCost * resource.getAmount();
        }
        return cost;
    }

}
