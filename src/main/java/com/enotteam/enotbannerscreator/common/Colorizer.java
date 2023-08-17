package com.enotteam.enotbannerscreator.common;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.List;
import java.util.stream.Collectors;

public class Colorizer {

    public static final LegacyComponentSerializer SERIALIZER = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    public static Component colorize(String input) {
        return SERIALIZER.deserialize(input);
    }

    public static List<Component> colorize(List<String> input) {
        return input.stream()
                .map(SERIALIZER::deserialize)
                .collect(Collectors.toList());
    }

}
