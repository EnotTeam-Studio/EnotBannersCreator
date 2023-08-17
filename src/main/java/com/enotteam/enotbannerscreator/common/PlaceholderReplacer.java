package com.enotteam.enotbannerscreator.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaceholderReplacer {

    public static String replacePlaceholders(String input, Map<String, String> placeholders) {
        final String[] output = {input};
        placeholders.forEach((placeholder, value) -> output[0] = output[0].replace(placeholder, value));
        return output[0];
    }

    public static List<String> replacePlaceholders(List<String> input, Map<String, String> placeholders) {
        List<String> output = new ArrayList<>();
        input.forEach(line -> {
            output.add(replacePlaceholders(line, placeholders));
        });
        return output;
    }

}
