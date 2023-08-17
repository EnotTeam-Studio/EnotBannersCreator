package com.enotteam.enotbannerscreator.common;

import dev.triumphteam.gui.components.util.VersionHelper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class LegacyMaterialParser {

    public static ItemStack parse(String stringMaterial) {
        if (!stringMaterial.contains(":")) {
            return new ItemStack(parseMaterial(stringMaterial));
        }
        String[] split = stringMaterial.split(":");
        if (VersionHelper.IS_ITEM_LEGACY) {
            return new ItemStack(parseMaterial(split[0]), 1, Short.parseShort(split[1]));
        }
        return new ItemStack(parseMaterial(split[0]));
    }

    private static Material parseMaterial(String stringMaterial) {
        Material material = Material.getMaterial(stringMaterial);
        if (material != null) return material;
        return Material.getMaterial(stringMaterial, true);
    }

}
