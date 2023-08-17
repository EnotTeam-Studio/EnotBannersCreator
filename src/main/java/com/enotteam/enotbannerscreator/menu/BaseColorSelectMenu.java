package com.enotteam.enotbannerscreator.menu;

import com.enotteam.enotbannerscreator.banner.common.BannerParser;
import com.enotteam.enotbannerscreator.common.Colorizer;
import com.enotteam.enotbannerscreator.common.Lang;
import com.enotteam.enotbannerscreator.menu.component.MenuFiller;
import com.enotteam.enotbannerscreator.menu.component.MenuItem;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BaseColorSelectMenu {

    private static final List<Integer> COLORS_SLOTS = Arrays.asList(
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29);

    MenuFiller menuFiller;

    Component title;
    MenuItem backItem;

    public BaseColorSelectMenu(MenuFiller menuFiller) {

        this.menuFiller = menuFiller;

        this.title = Colorizer.colorize(Lang.get().getString("gui.base-color.title"));
        this.backItem = MenuItem.fromSection(49, Lang.get().getConfigurationSection("gui.base-color.items.back"));

    }

    public void open(Player player) {

        Gui gui = Gui.gui()
                .rows(6)
                .title(title)
                .create();

        menuFiller.fill(gui);

        gui.setDefaultClickAction(event -> event.setCancelled(true));

        for (int i = 0; i < DyeColor.values().length; i++) {
            final DyeColor color = DyeColor.values()[i];
            gui.setItem(COLORS_SLOTS.get(i), ItemBuilder.banner().baseColor(color).asGuiItem(event -> {
                StaticMenus.getPatternSelectMenu().open(
                        player,
                        BannerParser.getCodeByBaseColor(color),
                        DyeColor.BLACK,
                        true);
            }));
        }

        gui.setItem(backItem.getSlot(), backItem.toItemBuilder().asGuiItem(event -> {
            StaticMenus.getMainMenu().open(player);
        }));

        gui.open(player);

    }

}
