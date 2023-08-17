package com.enotteam.enotbannerscreator.menu;

import com.enotteam.enotbannerscreator.banner.common.BannerParser;
import com.enotteam.enotbannerscreator.common.Colorizer;
import com.enotteam.enotbannerscreator.common.Lang;
import com.enotteam.enotbannerscreator.menu.component.MenuFiller;
import com.enotteam.enotbannerscreator.menu.component.MenuItem;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PremadeBannersMenu {

    MenuFiller filler;
    List<String> bannerValues;

    Component title;

    MenuItem backItem;
    MenuItem previousPageItem;
    MenuItem nextPageItem;

    public PremadeBannersMenu(MenuFiller filler, List<String> bannerValues) {

        this.filler = filler;
        this.bannerValues = bannerValues;

        this.title = Colorizer.colorize(Lang.get().getString("gui.premade.title"));
        this.backItem = MenuItem.fromSection(49, Lang.get().getConfigurationSection("gui.premade.items.back"));
        this.previousPageItem = MenuItem.fromSection(18, Lang.get().getConfigurationSection("gui.premade.items.previous-page"));
        this.nextPageItem = MenuItem.fromSection(26, Lang.get().getConfigurationSection("gui.premade.items.next-page"));

    }

    public void open(Player player) {

        PaginatedGui gui = Gui.paginated()
                .title(title)
                .rows(6)
                .create();

        gui.setDefaultClickAction(event -> event.setCancelled(true));

        filler.fillBorder(gui);
        filler.fillRow(gui, 5);

        gui.setItem(backItem.getSlot(), backItem.toItemBuilder().asGuiItem(event -> {
            StaticMenus.getMainMenu().open(player);
        }));

        bannerValues.forEach(value -> {
            gui.addItem(BannerParser.parseToBannerBuilder(value).asGuiItem(event -> {
                StaticMenus.getBannerInfoMenu().open(player, value);
            }));
        });

        gui.open(player);
        updateArrows(gui);

    }

    private void updateArrows(PaginatedGui paginatedGui) {

        int current = paginatedGui.getCurrentPageNum();

        if (current != 1) {
            paginatedGui.updateItem(previousPageItem.getSlot(), previousPageItem.toItemBuilder().asGuiItem(event -> {
                paginatedGui.previous();
            }));
        } else {
            paginatedGui.updateItem(previousPageItem.getSlot(), filler.getFiller());
        }

        if (current != paginatedGui.getNextPageNum()) {
            paginatedGui.updateItem(nextPageItem.getSlot(), nextPageItem.toItemBuilder().asGuiItem(event -> {
                paginatedGui.next();
            }));
        } else {
            paginatedGui.updateItem(nextPageItem.getSlot(), filler.getFiller());
        }

    }

}
