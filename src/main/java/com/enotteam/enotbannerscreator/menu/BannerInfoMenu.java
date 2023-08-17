package com.enotteam.enotbannerscreator.menu;

import com.enotteam.enotbannerscreator.banner.crafting.BannerCraftingManager;
import com.enotteam.enotbannerscreator.common.*;
import com.enotteam.enotbannerscreator.banner.common.BannerParser;
import com.enotteam.enotbannerscreator.banner.common.BannerUtil;
import com.enotteam.enotbannerscreator.banner.crafting.BannerCraftingType;
import com.enotteam.enotbannerscreator.database.cache.PlayerCache;
import com.enotteam.enotbannerscreator.economy.EconomyManager;
import com.enotteam.enotbannerscreator.menu.component.MenuFiller;
import com.enotteam.enotbannerscreator.menu.component.MenuItem;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BannerInfoMenu {

    private static final List<Integer> FILLER_SLOTS = List.of(
            2, 3, 4, 5, 7, 8, 12, 13, 17, 21, 30, 31, 35, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48);
    private static final List<Integer> MATERIALS_SLOTS = List.of(9, 10, 11, 18, 19, 20, 27, 28, 29, 36, 37, 38);
    private static final Map<Integer, Integer> CRAFT_SLOTS = Map.of(
            0, 14, 1, 15, 2, 16,
            3, 23, 4, 24, 5, 25,
            6, 32, 7, 33, 8, 34);

    MenuFiller filler;
    EconomyManager economyManager;
    BannerCraftingManager craftingManager;
    PlayerCache playerCache;

    Component title;

    MenuItem backItem;
    MenuItem deleteItem;
    MenuItem exportItem;
    MenuItem craftingFreeItem;
    MenuItem craftingBuyItem;
    MenuItem craftingResourcesItem;
    MenuItem craftingNoPermItem;
    MenuItem copyAndEditItem;

    MenuItem patternsInfoItem;
    MenuItem currentPageItem;
    MenuItem previousPageItem;
    MenuItem nextPageItem;

    public BannerInfoMenu(
            MenuFiller menuFiller,
            BannerCraftingManager bannerCraftingManager,
            EconomyManager economyManager,
            PlayerCache playerCache) {

        this.filler = menuFiller;
        this.craftingManager = bannerCraftingManager;
        this.economyManager = economyManager;
        this.playerCache = playerCache;

        this.title = Colorizer.colorize(Lang.get().getString("gui.info.title"));

        this.backItem = MenuItem.fromSection(49, Lang.get().getConfigurationSection("gui.info.items.back"));
        this.deleteItem = MenuItem.fromSection(50, Lang.get().getConfigurationSection("gui.info.items.delete"));
        this.exportItem = MenuItem.fromSection(51, Lang.get().getConfigurationSection("gui.info.items.export-to-mcgolem"));
        this.craftingFreeItem = MenuItem.fromSection(52, Lang.get().getConfigurationSection("gui.info.items.crafting-free"));
        this.craftingBuyItem = MenuItem.fromSection(52, Lang.get().getConfigurationSection("gui.info.items.crafting-buy"));
        this.craftingResourcesItem = MenuItem.fromSection(52, Lang.get().getConfigurationSection("gui.info.items.crafting-resources"));
        this.craftingNoPermItem = MenuItem.fromSection(52, Lang.get().getConfigurationSection("gui.info.items.crafting-no-perm"));
        this.copyAndEditItem = MenuItem.fromSection(53, Lang.get().getConfigurationSection("gui.info.items.copy-and-edit"));

        this.patternsInfoItem = MenuItem.fromSection(1, Lang.get().getConfigurationSection("gui.info.items.patterns"));
        this.currentPageItem = MenuItem.fromSection(6, Lang.get().getConfigurationSection("gui.info.items.current-page"));
        this.previousPageItem = MenuItem.fromSection(22, Lang.get().getConfigurationSection("gui.info.items.previous-page"));
        this.nextPageItem = MenuItem.fromSection(26, Lang.get().getConfigurationSection("gui.info.items.next-page"));

    }

    public void open(Player player, String code) {

        Gui gui = Gui.gui()
                .rows(6)
                .title(title)
                .create();

        gui.setDefaultClickAction(event -> event.setCancelled(true));

        filler.fill(gui, FILLER_SLOTS);

        ItemStack banner = BannerParser.parseToBannerBuilder(code).build();

        List<ItemStack> toCraft = BannerUtil.getItemsToCraft(banner);
        for (int i = 0; i < Math.min(MATERIALS_SLOTS.size(), toCraft.size()); i++) {
            gui.setItem(MATERIALS_SLOTS.get(i), ItemBuilder.from(toCraft.get(i)).asGuiItem());
        }

        int maxPages = BannerUtil.getCraftPagesAmount(banner);

        gui.setItem(0, ItemBuilder.from(banner).asGuiItem());
        gui.setItem(patternsInfoItem.getSlot(), patternsInfoItem
                .toItemBuilder(Map.of("{amount}", maxPages + ""))
                .asGuiItem());

        gui.setItem(backItem.getSlot(), backItem.toItemBuilder().asGuiItem(event -> {
            StaticMenus.getMainMenu().open(player);
        }));

        gui.setItem(deleteItem.getSlot(), deleteItem.toItemBuilder().asGuiItem(event -> {
            playerCache.get(player.getName()).getSavedBanners().remove(code);
            StaticMenus.getMainMenu().open(player);
        }));

        gui.setItem(exportItem.getSlot(), exportItem.toItemBuilder().asGuiItem(event -> {
            Messages.sendWithLink(player, "export-banner", McGolemLinkSerializer.serialize(code));
            gui.close(player);
        }));

        if (craftingManager.isNeedPermission() && !player.hasPermission(craftingManager.getPermission())) {
            gui.setItem(craftingNoPermItem.getSlot(), craftingNoPermItem.toItemBuilder().asGuiItem());
        } else if (craftingManager.getType() == BannerCraftingType.FREE) {
            gui.setItem(craftingFreeItem.getSlot(), craftingFreeItem.toItemBuilder().asGuiItem(event -> {
                player.getInventory().addItem(banner);
                Messages.send(player, "banner-create-successful");
                gui.close(player);
            }));
        } else if (craftingManager.getType() == BannerCraftingType.MONEY) {
            int cost = craftingManager.getCost(toCraft);
            gui.setItem(craftingBuyItem.getSlot(), craftingBuyItem.toItemBuilder(Map.of("{cost}", cost + "")).asGuiItem(event -> {

                if (!economyManager.has(player, cost)) {
                    Messages.send(player, "banner-create-not-enough-money");
                    gui.close(player);
                    return;
                }

                economyManager.take(player, cost);
                player.getInventory().addItem(banner);
                Messages.send(player, "banner-create-successful");
                gui.close(player);
                return;

            }));
        } else if (craftingManager.getType() == BannerCraftingType.RESOURCES) {
            gui.setItem(craftingResourcesItem.getSlot(), craftingResourcesItem.toItemBuilder().asGuiItem(event -> {

                if (BannerUtil.hasItems(player, toCraft)) {
                    BannerUtil.clearItems(player, toCraft);
                    player.getInventory().addItem(banner);
                    Messages.send(player, "banner-create-not-enough-resources");
                    gui.close(player);
                    return;
                }

                Messages.send(player, "banner-create-successful");
                gui.close(player);

            }));
        }

        gui.setItem(copyAndEditItem.getSlot(), copyAndEditItem.toItemBuilder().asGuiItem(event -> {
            StaticMenus.getPatternSelectMenu().open(player, code, DyeColor.BLACK, true);
        }));

        gui.open(player);

        updateCraftPage(gui, maxPages, 1, banner);

    }

    public void updateCraftPage(Gui gui, int maxPages, int page, ItemStack banner) {

        CRAFT_SLOTS.values().forEach(gui::removeItem);

        BannerUtil.getPatternCraft(banner, page).forEach((slot, item) -> {
            gui.updateItem(CRAFT_SLOTS.get(slot), ItemBuilder.from(item).asGuiItem());
        });

        gui.updateItem(currentPageItem.getSlot(), currentPageItem.toItemBuilder(Map.of(
                "{current}", page + "",
                "{max}", maxPages + "")).amount(page).asGuiItem());

        if (maxPages > page) {
            gui.updateItem(nextPageItem.getSlot(), nextPageItem.toItemBuilder().amount(page + 1).asGuiItem(event -> {
                updateCraftPage(gui, maxPages, page + 1, banner);
            }));
        } else {
            gui.updateItem(nextPageItem.getSlot(), filler.getFiller());
        }

        if (page != 1 && maxPages != 1) {
            gui.updateItem(previousPageItem.getSlot(), previousPageItem.toItemBuilder().amount(page - 1).asGuiItem(event -> {
                updateCraftPage(gui, maxPages, page - 1, banner);
            }));
        } else {
            gui.updateItem(previousPageItem.getSlot(), filler.getFiller());
        }

    }

}
