package com.enotteam.enotbannerscreator.common;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public class StringLocation {

    private static final String SPLIT_PATTERN = " ; ";

    @Nullable
    public static Location fromString(@NonNull String locationString) {

        String[] split = locationString.split(Pattern.quote(SPLIT_PATTERN));

        if (split.length == 4) {
            return new Location(
                    Bukkit.getWorld(split[0]),
                    Double.parseDouble(split[1]),
                    Double.parseDouble(split[2]),
                    Double.parseDouble(split[3]));
        }

        if (split.length >= 6) {
            return new Location(
                    Bukkit.getWorld(split[0]),
                    Double.parseDouble(split[1]),
                    Double.parseDouble(split[2]),
                    Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]),
                    Float.parseFloat(split[5]));
        }

        return null;

    }

    public static String toString(@NonNull Location location, boolean withPitchAndYaw) {

        String strLocation = location.getWorld().getName() + SPLIT_PATTERN
                + location.getBlockX() + SPLIT_PATTERN + location.getBlockY() + SPLIT_PATTERN + location.getBlockZ();

        if (withPitchAndYaw)
            strLocation += SPLIT_PATTERN + location.getYaw() + SPLIT_PATTERN + location.getPitch();

        return strLocation;

    }

}
