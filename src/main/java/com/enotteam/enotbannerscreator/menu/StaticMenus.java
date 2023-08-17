package com.enotteam.enotbannerscreator.menu;

public class StaticMenus {

    private static MainMenu mainMenu;
    private static BannerInfoMenu bannerInfoMenu;
    private static BaseColorSelectMenu baseColorSelectMenu;
    private static PatternSelectMenu patternSelectMenu;
    private static PremadeBannersMenu premadeBannersMenu;

    public static void set(
            MainMenu main,
            BannerInfoMenu banner,
            PatternSelectMenu pattern,
            BaseColorSelectMenu baseColor,
            PremadeBannersMenu premade) {
        mainMenu = main;
        bannerInfoMenu = banner;
        baseColorSelectMenu = baseColor;
        patternSelectMenu = pattern;
        premadeBannersMenu = premade;
    }

    public static MainMenu getMainMenu() {
        return mainMenu;
    }

    public static BannerInfoMenu getBannerInfoMenu() {
        return bannerInfoMenu;
    }

    public static PatternSelectMenu getPatternSelectMenu() {
        return patternSelectMenu;
    }

    public static BaseColorSelectMenu getBaseColorSelectMenu() {
        return baseColorSelectMenu;
    }

    public static PremadeBannersMenu getPremadeBannersMenu() {
        return premadeBannersMenu;
    }
}
