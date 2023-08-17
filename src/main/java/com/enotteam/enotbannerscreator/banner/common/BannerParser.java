package com.enotteam.enotbannerscreator.banner.common;

import dev.triumphteam.gui.builder.item.BannerBuilder;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

import java.util.*;

public class BannerParser {

    private static final String CODE_REGEX = "(?<=\\G.{2})";

    public static BannerBuilder parseToBannerBuilder(String code) {

        String[] split = code.split(CODE_REGEX);

        BannerBuilder builder = ItemBuilder.banner();

        List<Pattern> patterns = new ArrayList<>();

        Iterator<String> iterator = Arrays.stream(split).iterator();
        patterns.add(new Pattern(getDyeColor(iterator.next()), PatternType.BASE));
        iterator.forEachRemaining(value -> patterns.add(new Pattern(getDyeColor(value), getPatternType(value))));

        builder.setPatterns(patterns);

        return builder;

    }

    public static boolean isCodeCanBeParsed(String code) {

        if (code.length() < 2 || code.length() % 2 != 0) return false;

        String[] split = code.split(CODE_REGEX);

        Iterator<String> iterator = Arrays.stream(split).iterator();
        if (getDyeColor(iterator.next()) == null) return false;

        while (iterator.hasNext()) {
            String value = iterator.next();
            if (DyeColorValue.getByName(value.charAt(0)+"") == null
                    || PatternTypeValue.getByName(value.charAt(1)+"") == null) return false;
        }

        return true;

    }

    public static DyeColor getDyeColor(String code) {
        return DyeColorValue.valueOf(String.valueOf(code.charAt(0))).getColor();
    }

    public static PatternType getPatternType(String code) {
        return PatternTypeValue.valueOf(String.valueOf(code.charAt(1))).getType();
    }

    public static String getCodeByBaseColor(DyeColor baseColor) {
        return DyeColorValue.getByDyeColor(baseColor).name() + "a";
    }

    public static String getCodeByPatternAndColor(PatternType patternType, DyeColor dyeColor) {
        return DyeColorValue.getByDyeColor(dyeColor) + PatternTypeValue.getByPatternType(patternType).toString();
    }

    @AllArgsConstructor
    private enum DyeColorValue {

        a(DyeColor.BLACK),
        i(DyeColor.GRAY),
        h(DyeColor.getByDyeData((byte) 0x7)),
        p(DyeColor.WHITE),
        j(DyeColor.PINK),
        n(DyeColor.MAGENTA),
        f(DyeColor.PURPLE),
        e(DyeColor.BLUE),
        g(DyeColor.CYAN),
        m(DyeColor.LIGHT_BLUE),
        c(DyeColor.GREEN),
        k(DyeColor.LIME),
        l(DyeColor.YELLOW),
        o(DyeColor.ORANGE),
        d(DyeColor.BROWN),
        b(DyeColor.RED);

        @Getter
        private final DyeColor color;

        public static DyeColorValue getByDyeColor(DyeColor dyeColor) {
            return Arrays.stream(values())
                    .filter(dyeColorValues -> dyeColorValues.getColor() == dyeColor)
                    .findAny()
                    .get();
        }

        public static DyeColorValue getByName(String name) {
            for (DyeColorValue value : values()) {
                if (value.name().equals(name)) {
                    return value;
                }
            }
            return null;
        }

    }

    @AllArgsConstructor
    private enum PatternTypeValue {

        p(PatternType.GRADIENT),
        K(PatternType.GRADIENT_UP),
        e(PatternType.BRICKS),
        q(PatternType.HALF_HORIZONTAL),
        L(PatternType.HALF_HORIZONTAL_MIRROR),
        H(PatternType.HALF_VERTICAL),
        M(PatternType.HALF_VERTICAL_MIRROR),
        E(PatternType.STRIPE_TOP),
        f(PatternType.STRIPE_BOTTOM),
        s(PatternType.STRIPE_LEFT),
        y(PatternType.STRIPE_RIGHT),
        r(PatternType.DIAGONAL_LEFT),
        J(PatternType.DIAGONAL_RIGHT_MIRROR),
        I(PatternType.DIAGONAL_LEFT_MIRROR),
        x(PatternType.DIAGONAL_RIGHT),
        j(PatternType.CROSS),
        m(PatternType.STRIPE_DOWNLEFT),
        n(PatternType.STRIPE_DOWNRIGHT),
        z(PatternType.STRAIGHT_CROSS),
        l(PatternType.STRIPE_CENTER),
        w(PatternType.STRIPE_MIDDLE),
        C(PatternType.SQUARE_TOP_LEFT),
        b(PatternType.SQUARE_BOTTOM_LEFT),
        D(PatternType.SQUARE_TOP_RIGHT),
        d(PatternType.SQUARE_BOTTOM_RIGHT),
        F(PatternType.TRIANGLE_TOP),
        g(PatternType.TRIANGLE_BOTTOM),
        v(PatternType.RHOMBUS_MIDDLE),
        t(PatternType.CIRCLE_MIDDLE),
        h(PatternType.TRIANGLES_BOTTOM),
        G(PatternType.TRIANGLES_TOP),
        B(PatternType.STRIPE_SMALL),
        c(PatternType.BORDER),
        i(PatternType.CURLY_BORDER),
        o(PatternType.FLOWER),
        k(PatternType.CREEPER),
        A(PatternType.SKULL),
        u(PatternType.MOJANG);

        public static PatternTypeValue getByPatternType(PatternType type) {
            return Arrays.stream(values())
                    .filter(patternTypeValue -> patternTypeValue.getType() == type)
                    .findAny()
                    .get();
        }

        public static PatternTypeValue getByName(String name) {
            for (PatternTypeValue  value : values()) {
                if (value.name().equals(name)) {
                    return value;
                }
            }
            return null;
        }

        @Getter
        private final PatternType type;

    }

}
