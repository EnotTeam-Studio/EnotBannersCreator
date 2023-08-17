package com.enotteam.enotbannerscreator.menu.component;

import com.enotteam.enotbannerscreator.common.LegacyMaterialParser;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuFiller {

    @Getter
    GuiItem filler;

    public MenuFiller(ConfigurationSection section) {
        filler = ItemBuilder.from(LegacyMaterialParser.parse(section.getString("filler-material")))
                .name(Component.text(" "))
                .asGuiItem();
    }

    public void fill(BaseGui gui) {
        gui.getFiller().fill(filler);
    }

    public void fillBorder(BaseGui gui) {
        gui.getFiller().fillBorder(filler);
    }

    public void fillRow(BaseGui gui, int row) {
        gui.getFiller().fillBetweenPoints(row, 0, row, 8, filler);
    }

    public void fill(BaseGui gui, int... slots) {
        for (int slot : slots) {
            gui.setItem(slot, filler);
        }
    }

    public void fill(BaseGui gui, List<Integer> fillerSlots) {
        fillerSlots.forEach(slot -> gui.setItem(slot, filler));
    }
}
