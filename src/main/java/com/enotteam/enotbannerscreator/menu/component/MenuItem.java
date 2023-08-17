package com.enotteam.enotbannerscreator.menu.component;

import com.enotteam.enotbannerscreator.common.Colorizer;
import com.enotteam.enotbannerscreator.common.LegacyMaterialParser;
import com.enotteam.enotbannerscreator.common.PlaceholderReplacer;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuItem {

    int slot;
    ItemStack item;
    String name;
    List<String> lore;

    public static MenuItem fromSection(int slot, ConfigurationSection section) {
        return new MenuItem(
                slot,
                LegacyMaterialParser.parse(section.getString("material", "STONE")),
                section.getString("name"),
                section.getStringList("lore"));
    }

    public ItemBuilder toItemBuilder() {
        return ItemBuilder.from(item)
                .name(Colorizer.colorize(name))
                .lore(Colorizer.colorize(lore));
    }

    public ItemBuilder toItemBuilder(Map<String, String> placeholders) {
        return ItemBuilder.from(item)
                .name(Colorizer.colorize(PlaceholderReplacer.replacePlaceholders(name, placeholders)))
                .lore(Colorizer.colorize(PlaceholderReplacer.replacePlaceholders(lore, placeholders)));
    }

}
