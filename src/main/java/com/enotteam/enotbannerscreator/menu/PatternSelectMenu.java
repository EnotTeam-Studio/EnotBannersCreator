package com.enotteam.enotbannerscreator.menu;

import com.enotteam.enotbannerscreator.banner.common.BannerParser;
import com.enotteam.enotbannerscreator.common.Colorizer;
import com.enotteam.enotbannerscreator.common.Lang;
import com.enotteam.enotbannerscreator.database.cache.PlayerCache;
import com.enotteam.enotbannerscreator.menu.component.MenuItem;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PatternSelectMenu {

    private static final List<Integer> PATTERN_SLOTS = Arrays.asList(
            19, 20, 21, 22, 23, 24, 25, 26,
            28, 29, 30, 31, 32, 33, 34, 35,
            37, 38, 39, 40, 41, 42, 43, 44);

    int maxBannerValueLength;
    PlayerCache playerCache;

    Component title;

    MenuItem selectColorBlackItem;
    MenuItem selectColorRedItem;
    MenuItem selectColorGreenItem;
    MenuItem selectColorBrownItem;
    MenuItem selectColorBlueItem;
    MenuItem selectColorPurpleItem;
    MenuItem selectColorCyanItem;
    MenuItem selectColorLightGrayItem;
    MenuItem selectColorGrayItem;
    MenuItem selectColorPinkItem;
    MenuItem selectColorLimeItem;
    MenuItem selectColorYellowItem;
    MenuItem selectColorLightBlueItem;
    MenuItem selectColorMagentaItem;
    MenuItem selectColorOrangeItem;
    MenuItem selectColorWhiteItem;

    MenuItem backItem;
    MenuItem deleteItem;
    MenuItem removeLastPatternItem;
    MenuItem morePatternsItem;
    MenuItem saveBannerItem;

    public PatternSelectMenu(int maxBannerValueLength, PlayerCache playerCache) {

        this.maxBannerValueLength = maxBannerValueLength;
        this.playerCache = playerCache;

        this.title = Colorizer.colorize(Lang.get().getString("gui.patterns.title"));

        this.selectColorBlackItem = MenuItem.fromSection(1, Lang.get().getConfigurationSection("gui.patterns.items.color-black"));
        this.selectColorRedItem = MenuItem.fromSection(2, Lang.get().getConfigurationSection("gui.patterns.items.color-red"));
        this.selectColorGreenItem = MenuItem.fromSection(3, Lang.get().getConfigurationSection("gui.patterns.items.color-green"));
        this.selectColorBrownItem = MenuItem.fromSection(4, Lang.get().getConfigurationSection("gui.patterns.items.color-brown"));
        this.selectColorBlueItem = MenuItem.fromSection(5, Lang.get().getConfigurationSection("gui.patterns.items.color-blue"));
        this.selectColorPurpleItem = MenuItem.fromSection(6, Lang.get().getConfigurationSection("gui.patterns.items.color-purple"));
        this.selectColorCyanItem = MenuItem.fromSection(7, Lang.get().getConfigurationSection("gui.patterns.items.color-cyan"));
        this.selectColorLightGrayItem = MenuItem.fromSection(8, Lang.get().getConfigurationSection("gui.patterns.items.color-light-gray"));
        this.selectColorGrayItem = MenuItem.fromSection(10, Lang.get().getConfigurationSection("gui.patterns.items.color-gray"));
        this.selectColorPinkItem = MenuItem.fromSection(11, Lang.get().getConfigurationSection("gui.patterns.items.color-pink"));
        this.selectColorLimeItem = MenuItem.fromSection(12, Lang.get().getConfigurationSection("gui.patterns.items.color-lime"));
        this.selectColorYellowItem = MenuItem.fromSection(13, Lang.get().getConfigurationSection("gui.patterns.items.color-yellow"));
        this.selectColorLightBlueItem = MenuItem.fromSection(14, Lang.get().getConfigurationSection("gui.patterns.items.color-light-blue"));
        this.selectColorMagentaItem = MenuItem.fromSection(15, Lang.get().getConfigurationSection("gui.patterns.items.color-magenta"));
        this.selectColorOrangeItem = MenuItem.fromSection(16, Lang.get().getConfigurationSection("gui.patterns.items.color-orange"));
        this.selectColorWhiteItem = MenuItem.fromSection(17, Lang.get().getConfigurationSection("gui.patterns.items.color-white"));

        this.backItem = MenuItem.fromSection(45, Lang.get().getConfigurationSection("gui.patterns.items.back"));
        this.deleteItem = MenuItem.fromSection(47, Lang.get().getConfigurationSection("gui.patterns.items.delete"));
        this.removeLastPatternItem = MenuItem.fromSection(49, Lang.get().getConfigurationSection("gui.patterns.items.remove-last-pattern"));
        this.morePatternsItem = MenuItem.fromSection(51, Lang.get().getConfigurationSection("gui.patterns.items.more-patterns"));
        this.saveBannerItem = MenuItem.fromSection(53, Lang.get().getConfigurationSection("gui.patterns.items.save-banner"));

    }

    public void open(Player player, String bannerCode, DyeColor patternColor, boolean firstPatternPage) {

        String code = bannerCode.length() > maxBannerValueLength ? bannerCode.substring(0, 32) : bannerCode;

        Gui gui = Gui.gui()
                .rows(6)
                .title(title)
                .create();

        gui.setDefaultClickAction(event -> event.setCancelled(true));

        gui.setItem(backItem.getSlot(), backItem.toItemBuilder().asGuiItem(event -> {
            StaticMenus.getMainMenu().open(player);
        }));
        gui.setItem(deleteItem.getSlot(), deleteItem.toItemBuilder().asGuiItem(event -> {
            playerCache.get(player.getName()).getSavedBanners().remove(code);
            StaticMenus.getMainMenu().open(player);
        }));

        if (code.length() >= 4)
            gui.setItem(removeLastPatternItem.getSlot(), removeLastPatternItem.toItemBuilder().asGuiItem(event -> {
                this.open(player, code.substring(0, code.length() - 2), patternColor, firstPatternPage);
            }));

        gui.setItem(selectColorBlackItem.getSlot(), selectColorBlackItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.BLACK, true);
        }));
        gui.setItem(selectColorRedItem.getSlot(), selectColorRedItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.RED, true);
        }));
        gui.setItem(selectColorGreenItem.getSlot(), selectColorGreenItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.GREEN, true);
        }));
        gui.setItem(selectColorBrownItem.getSlot(), selectColorBrownItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.BROWN, true);
        }));
        gui.setItem(selectColorBlueItem.getSlot(), selectColorBlueItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.BLUE, true);
        }));
        gui.setItem(selectColorPurpleItem.getSlot(), selectColorPurpleItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.PURPLE, true);
        }));
        gui.setItem(selectColorCyanItem.getSlot(), selectColorCyanItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.CYAN, true);
        }));
        gui.setItem(selectColorLightGrayItem.getSlot(), selectColorLightGrayItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.getByDyeData((byte) 0x7), true);
        }));
        gui.setItem(selectColorGrayItem.getSlot(), selectColorGrayItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.GRAY, true);
        }));
        gui.setItem(selectColorPinkItem.getSlot(), selectColorPinkItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.PINK, true);
        }));
        gui.setItem(selectColorLimeItem.getSlot(), selectColorLimeItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.LIME, true);
        }));
        gui.setItem(selectColorYellowItem.getSlot(), selectColorYellowItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.YELLOW, true);
        }));
        gui.setItem(selectColorLightBlueItem.getSlot(), selectColorLightBlueItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.LIGHT_BLUE, true);
        }));
        gui.setItem(selectColorMagentaItem.getSlot(), selectColorMagentaItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.MAGENTA, true);
        }));
        gui.setItem(selectColorOrangeItem.getSlot(), selectColorOrangeItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.ORANGE, true);
        }));
        gui.setItem(selectColorWhiteItem.getSlot(), selectColorWhiteItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, DyeColor.WHITE, true);
        }));

        gui.setItem(0, BannerParser.parseToBannerBuilder(code).asGuiItem());

        refreshPatterns(player, code, gui, BannerParser.getDyeColor(code), patternColor, firstPatternPage);

        gui.setItem(morePatternsItem.getSlot(), morePatternsItem.toItemBuilder().asGuiItem(event -> {
            this.open(player, code, patternColor, !firstPatternPage);
        }));

        gui.setItem(saveBannerItem.getSlot(), saveBannerItem.toItemBuilder().asGuiItem(event -> {
            playerCache.get(player.getName()).getSavedBanners().add(code);
            StaticMenus.getBannerInfoMenu().open(player, code);
        }));

        gui.open(player);

    }

    private void refreshPatterns(Player player, String code, Gui gui, DyeColor baseColor, DyeColor patternColor, boolean firstPage) {

        if (firstPage) {

            for (int i = 1; i < PATTERN_SLOTS.size() + 1; i++) {
                PatternType patternType = PatternType.values()[i];
                gui.setItem(PATTERN_SLOTS.get(i - 1),
                        ItemBuilder.banner()
                                .baseColor(baseColor)
                                .pattern(patternColor, patternType)
                                .asGuiItem(event -> {
                                    this.open(
                                            player,
                                            code + BannerParser.getCodeByPatternAndColor(patternType, patternColor),
                                            patternColor,
                                            true);
                                }));
            }

            return;

        }

        for (int i = 0; i < 14; i++) {
            PatternType patternType = PatternType.values()[i + 24];
            gui.setItem(PATTERN_SLOTS.get(i),
                    ItemBuilder.banner()
                            .baseColor(baseColor)
                            .pattern(patternColor, patternType)
                            .asGuiItem(event -> {
                                this.open(
                                        player,
                                        code + BannerParser.getCodeByPatternAndColor(patternType, patternColor),
                                        patternColor,
                                        true);
                            }));
        }


    }

}
