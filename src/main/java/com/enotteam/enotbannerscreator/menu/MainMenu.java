package com.enotteam.enotbannerscreator.menu;

import com.enotteam.enotbannerscreator.banner.common.BannerParser;
import com.enotteam.enotbannerscreator.banner.importing.BannerImportManager;
import com.enotteam.enotbannerscreator.common.Colorizer;
import com.enotteam.enotbannerscreator.common.Lang;
import com.enotteam.enotbannerscreator.common.Messages;
import com.enotteam.enotbannerscreator.database.cache.PlayerCache;
import com.enotteam.enotbannerscreator.menu.component.MenuFiller;
import com.enotteam.enotbannerscreator.menu.component.MenuItem;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MainMenu {

    MenuFiller filler;
    BannerImportManager bannerImportManager;
    BaseColorSelectMenu baseColorSelectMenu;
    PlayerCache playerCache;

    Component title;

    MenuItem golemImportItem;
    MenuItem createBannerItem;
    MenuItem premadeBannersItem;
    MenuItem previousPageItem;
    MenuItem nextPageItem;

    public MainMenu(MenuFiller menuFiller, BannerImportManager bannerImportManager, BaseColorSelectMenu baseColorSelectMenu, PlayerCache playerCache) {

        this.filler = menuFiller;
        this.bannerImportManager = bannerImportManager;
        this.baseColorSelectMenu = baseColorSelectMenu;
        this.playerCache = playerCache;

        this.title = Colorizer.colorize(Lang.get().getString("gui.main.title"));
        this.golemImportItem = MenuItem.fromSection(47, Lang.get().getConfigurationSection("gui.main.items.mcgolem-import"));
        this.createBannerItem = MenuItem.fromSection(49, Lang.get().getConfigurationSection("gui.main.items.create-banner"));
        this.premadeBannersItem = MenuItem.fromSection(51, Lang.get().getConfigurationSection("gui.main.items.premade-banners"));
        this.previousPageItem = MenuItem.fromSection(18, Lang.get().getConfigurationSection("gui.main.items.previous-page"));
        this.nextPageItem = MenuItem.fromSection(26, Lang.get().getConfigurationSection("gui.main.items.next-page"));

    }

    public void open(Player player) {

        PaginatedGui gui = Gui.paginated()
                .title(this.title)
                .rows(6)
                .create();

        gui.setDefaultClickAction(event -> event.setCancelled(true));

        filler.fillBorder(gui);
        filler.fillRow(gui, 5);

        gui.setItem(golemImportItem.getSlot(), golemImportItem.toItemBuilder().asGuiItem(event -> {
            bannerImportManager.addToImportWaiting(player);
            Messages.send(player, "import-banner");
            gui.close(player);
        }));

        gui.setItem(createBannerItem.getSlot(), createBannerItem.toItemBuilder().asGuiItem(event -> {
            baseColorSelectMenu.open(player);
        }));

        gui.setItem(premadeBannersItem.getSlot(), premadeBannersItem.toItemBuilder().asGuiItem(event -> {
            StaticMenus.getPremadeBannersMenu().open(player);
        }));

        playerCache.get(player.getName()).getSavedBanners().forEach(code -> {
            gui.addItem(BannerParser.parseToBannerBuilder(code).asGuiItem(event -> {
                StaticMenus.getBannerInfoMenu().open(player, code);
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
