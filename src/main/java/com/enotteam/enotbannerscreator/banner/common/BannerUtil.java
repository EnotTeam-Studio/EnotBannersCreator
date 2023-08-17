package com.enotteam.enotbannerscreator.banner.common;

import com.enotteam.enotbannerscreator.common.LegacyMaterialParser;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.*;
import java.util.stream.Collectors;

public class BannerUtil {

    private static final HashMap<DyeColor, Integer> DYE_COLORS = new HashMap<>();

    static {

        DYE_COLORS.put(DyeColor.BLACK, 0);
        DYE_COLORS.put(DyeColor.RED, 1);
        DYE_COLORS.put(DyeColor.GREEN, 2);
        DYE_COLORS.put(DyeColor.BROWN, 3);
        DYE_COLORS.put(DyeColor.BLUE, 4);
        DYE_COLORS.put(DyeColor.PURPLE, 5);
        DYE_COLORS.put(DyeColor.CYAN, 6);
        DYE_COLORS.put(DyeColor.getByDyeData((byte) 0x7), 7);
        DYE_COLORS.put(DyeColor.GRAY, 8);
        DYE_COLORS.put(DyeColor.PINK, 9);
        DYE_COLORS.put(DyeColor.LIME, 10);
        DYE_COLORS.put(DyeColor.YELLOW, 11);
        DYE_COLORS.put(DyeColor.LIGHT_BLUE, 12);
        DYE_COLORS.put(DyeColor.MAGENTA, 13);
        DYE_COLORS.put(DyeColor.ORANGE, 14);
        DYE_COLORS.put(DyeColor.WHITE, 15);

    }

    public static List<ItemStack> getItemsToCraft(ItemStack banner) {

        BannerMeta meta = (BannerMeta) banner.getItemMeta();
        Inventory materials = Bukkit.createInventory(null, 54);

        meta.getPatterns().forEach(pattern -> {
            DyeColor color = pattern.getColor();
            switch (pattern.getPattern()) {
                case BASE:
                    materials.addItem(new ItemStack(Material.STICK));
                    materials.addItem(toWoolItemStack(color, 6));
                    break;
                case SQUARE_BOTTOM_LEFT:
                case SQUARE_BOTTOM_RIGHT:
                case SQUARE_TOP_LEFT:
                case SQUARE_TOP_RIGHT:
                case CIRCLE_MIDDLE:
                    materials.addItem(toDyeItemStack(color, 1));
                    break;
                case STRIPE_BOTTOM:
                case STRIPE_TOP:
                case STRIPE_LEFT:
                case STRIPE_RIGHT:
                case STRIPE_CENTER:
                case STRIPE_MIDDLE:
                case STRIPE_DOWNRIGHT:
                case STRIPE_DOWNLEFT:
                case TRIANGLE_BOTTOM:
                case TRIANGLE_TOP:
                case TRIANGLES_BOTTOM:
                case TRIANGLES_TOP:
                case DIAGONAL_LEFT:
                case DIAGONAL_RIGHT:
                case DIAGONAL_LEFT_MIRROR:
                case DIAGONAL_RIGHT_MIRROR:
                    materials.addItem(toDyeItemStack(color, 3));
                    break;
                case STRIPE_SMALL:
                case RHOMBUS_MIDDLE:
                case GRADIENT:
                case GRADIENT_UP:
                    materials.addItem(toDyeItemStack(color, 4));
                    break;
                case CROSS:
                case STRAIGHT_CROSS:
                    materials.addItem(toDyeItemStack(color, 5));
                    break;
                case HALF_VERTICAL:
                case HALF_HORIZONTAL:
                case HALF_VERTICAL_MIRROR:
                case HALF_HORIZONTAL_MIRROR:
                    materials.addItem(toDyeItemStack(color, 6));
                    break;
                case BORDER:
                    materials.addItem(toDyeItemStack(color, 8));
                    break;
                case CURLY_BORDER:
                    materials.addItem(new ItemStack(Material.VINE));
                    if (!pattern.getColor().equals(DyeColor.BLACK)) {
                        materials.addItem(toDyeItemStack(color, 1));
                    }
                    break;
                case CREEPER:
                    materials.addItem(LegacyMaterialParser.parse("SKULL:4"));
                    if (!pattern.getColor().equals(DyeColor.BLACK)) {
                        materials.addItem(toDyeItemStack(color, 1));
                    }
                    break;
                case BRICKS:
                    materials.addItem(new ItemStack(Material.BRICK));
                    if (!pattern.getColor().equals(DyeColor.BLACK)) {
                        materials.addItem(toDyeItemStack(color, 1));
                    }
                    break;
                case SKULL:
                    materials.addItem(LegacyMaterialParser.parse("SKULL:1"));
                    if (!pattern.getColor().equals(DyeColor.BLACK)) {
                        materials.addItem(toDyeItemStack(color, 1));
                    }
                    break;
                case FLOWER:
                    materials.addItem(LegacyMaterialParser.parse("RED_ROSE:8"));
                    if (!pattern.getColor().equals(DyeColor.BLACK)) {
                        materials.addItem(toDyeItemStack(color, 1));
                    }
                    break;
                case MOJANG:
                    materials.addItem(LegacyMaterialParser.parse("GOLDEN_APPLE:1"));
                    if (!pattern.getColor().equals(DyeColor.BLACK)) {
                        materials.addItem(toDyeItemStack(color, 1));
                    }
                    break;
            }
        });

        return Arrays.stream(materials.getStorageContents())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

    }

    public static int getCraftPagesAmount(ItemStack banner) {
        return ((BannerMeta) banner.getItemMeta()).getPatterns().size();
    }

    public static HashMap<Integer, ItemStack> getPatternCraft(ItemStack banner, int page) {

        HashMap<Integer, ItemStack> craft = new HashMap<>();

        BannerMeta meta = (BannerMeta) banner.getItemMeta();
        int totalPages = getCraftPagesAmount(banner);

        if (page == 1) {
            ItemStack wool = toWoolItemStack(meta.getPattern(0).getColor(), 1);
            for (int i = 0; i < 6; i++) {
                craft.put(i, wool);
            }
            craft.put(7, new ItemStack(Material.STICK));
            return craft;
        } else if (page <= totalPages) {

            ItemStack prevBanner = banner.clone();
            BannerMeta prevBannerMeta = (BannerMeta) prevBanner.getItemMeta();
            prevBannerMeta.setPatterns(prevBannerMeta.getPatterns().stream()
                    .limit(page)
                    .collect(Collectors.toList()));
            prevBanner.setItemMeta(prevBannerMeta);

            int bannerPosition = 4;
            List<Integer> dyePositions = new ArrayList<>();

            Pattern pattern = prevBannerMeta.getPattern(page - 1);
            ItemStack dyeItem = toDyeItemStack(pattern.getColor(), 1);

            switch (pattern.getPattern()) {
                case SQUARE_BOTTOM_LEFT:
                    dyePositions = Collections.singletonList(6);
                    break;
                case SQUARE_BOTTOM_RIGHT:
                    dyePositions = Collections.singletonList(8);
                    break;
                case SQUARE_TOP_LEFT:
                    dyePositions = Collections.singletonList(0);
                    break;
                case SQUARE_TOP_RIGHT:
                    dyePositions = Collections.singletonList(2);
                    break;
                case STRIPE_BOTTOM:
                    dyePositions = Arrays.asList(6, 7, 8);
                    break;
                case STRIPE_TOP:
                    dyePositions = Arrays.asList(0, 1, 2);
                    break;
                case STRIPE_LEFT:
                    dyePositions = Arrays.asList(0, 3, 6);
                    break;
                case STRIPE_RIGHT:
                    dyePositions = Arrays.asList(2, 5, 8);
                    break;
                case STRIPE_CENTER:
                    bannerPosition = 3;
                    dyePositions = Arrays.asList(1, 4, 7);
                    break;
                case STRIPE_MIDDLE:
                    bannerPosition = 1;
                    dyePositions = Arrays.asList(3, 4, 5);
                    break;
                case STRIPE_DOWNRIGHT:
                    bannerPosition = 1;
                    dyePositions = Arrays.asList(0, 4, 8);
                    break;
                case STRIPE_DOWNLEFT:
                    bannerPosition = 1;
                    dyePositions = Arrays.asList(2, 4, 6);
                    break;
                case STRIPE_SMALL:
                    dyePositions = Arrays.asList(0, 2, 3, 5);
                    break;
                case CROSS:
                    bannerPosition = 1;
                    dyePositions = Arrays.asList(0, 2, 4, 6, 8);
                    break;
                case STRAIGHT_CROSS:
                    bannerPosition = 0;
                    dyePositions = Arrays.asList(1, 3, 4, 5, 7);
                    break;
                case TRIANGLE_BOTTOM:
                    bannerPosition = 7;
                    dyePositions = Arrays.asList(4, 6, 8);
                    break;
                case TRIANGLE_TOP:
                    bannerPosition = 1;
                    dyePositions = Arrays.asList(0, 2, 4);
                    break;
                case TRIANGLES_BOTTOM:
                    dyePositions = Arrays.asList(3, 5, 7);
                    break;
                case TRIANGLES_TOP:
                    dyePositions = Arrays.asList(1, 3, 5);
                    break;
                case DIAGONAL_LEFT:
                    dyePositions = Arrays.asList(0, 1, 3);
                    break;
                case DIAGONAL_RIGHT:
                    dyePositions = Arrays.asList(5, 7, 8);
                    break;
                case DIAGONAL_LEFT_MIRROR:
                    dyePositions = Arrays.asList(3, 6, 7);
                    break;
                case DIAGONAL_RIGHT_MIRROR:
                    dyePositions = Arrays.asList(1, 2, 5);
                    break;
                case CIRCLE_MIDDLE:
                    bannerPosition = 1;
                    dyePositions = Collections.singletonList(4);
                    break;
                case RHOMBUS_MIDDLE:
                    dyePositions = Arrays.asList(1, 3, 5, 7);
                    break;
                case HALF_VERTICAL:
                    bannerPosition = 5;
                    dyePositions = Arrays.asList(0, 1, 3, 4, 6, 7);
                    break;
                case HALF_HORIZONTAL:
                    bannerPosition = 7;
                    dyePositions = Arrays.asList(0, 1, 2, 3, 4, 5);
                    break;
                case HALF_VERTICAL_MIRROR:
                    bannerPosition = 3;
                    dyePositions = Arrays.asList(1, 2, 4, 5, 7, 8);
                    break;
                case HALF_HORIZONTAL_MIRROR:
                    bannerPosition = 1;
                    dyePositions = Arrays.asList(3, 4, 5, 6, 7, 8);
                    break;
                case BORDER:
                    dyePositions = Arrays.asList(0, 1, 2, 3, 5, 6, 7, 8);
                    break;
                case CURLY_BORDER:
                    craft.put(1, new ItemStack(Material.VINE));
                    if (!pattern.getColor().equals(DyeColor.BLACK)) {
                        dyePositions = Collections.singletonList(7);
                    }
                    break;
                case CREEPER:
                    craft.put(1, LegacyMaterialParser.parse("SKULL:4"));
                    if (!pattern.getColor().equals(DyeColor.BLACK)) {
                        dyePositions = Collections.singletonList(7);
                    }
                    break;
                case GRADIENT:
                    bannerPosition = 1;
                    dyePositions = Arrays.asList(0, 2, 4, 7);
                    break;
                case GRADIENT_UP:
                    bannerPosition = 7;
                    dyePositions = Arrays.asList(1, 4, 6, 8);
                    break;
                case BRICKS:
                    craft.put(1, new ItemStack(Material.BRICK));
                    if (!pattern.getColor().equals(DyeColor.BLACK)) {
                        dyePositions = Collections.singletonList(7);
                    }
                    break;
                case SKULL:
                    craft.put(1, LegacyMaterialParser.parse("SKULL:1"));
                    if (!pattern.getColor().equals(DyeColor.BLACK)) {
                        dyePositions = Collections.singletonList(7);
                    }
                    break;
                case FLOWER:
                    craft.put(1, LegacyMaterialParser.parse("RED_ROSE:8"));
                    if (!pattern.getColor().equals(DyeColor.BLACK)) {
                        dyePositions = Collections.singletonList(7);
                    }
                    break;
                case MOJANG:
                    craft.put(1, LegacyMaterialParser.parse("GOLDEN_APPLE:1"));
                    if (!pattern.getColor().equals(DyeColor.BLACK)) {
                        dyePositions = Collections.singletonList(7);
                    }
                    break;
            }

            craft.put(bannerPosition, prevBanner);
            for (int i : dyePositions) {
                craft.put(i, dyeItem.clone());
            }

        }

        return craft;

    }

    private static ItemStack toDyeItemStack(DyeColor color, int amount) {
        ItemStack item = LegacyMaterialParser.parse("INK_SACK:" + DYE_COLORS.get(color));
        item.setAmount(amount);
        return item;
    }

    private static ItemStack toWoolItemStack(DyeColor color, int amount) {
        ItemStack item = LegacyMaterialParser.parse("WOOL:" + Math.abs(15 - DYE_COLORS.get(color)));
        item.setAmount(amount);
        return item;
    }

    public static boolean hasItems(Player player, List<ItemStack> items) {
        boolean has = true;
        for (ItemStack item : items) {
            if (!player.getInventory().containsAtLeast(item, item.getAmount())) has = false;
        }
        return has;
    }

    public static void clearItems(Player player, List<ItemStack> items) {
        for (ItemStack item : items) {
            player.getInventory().remove(item);
        }
    }

}
