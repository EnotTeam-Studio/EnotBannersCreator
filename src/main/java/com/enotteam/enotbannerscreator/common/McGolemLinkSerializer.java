package com.enotteam.enotbannerscreator.common;

public class McGolemLinkSerializer {

    private static final String HTTP_LINK = "http://mcgolem.com/services/banner?=";
    private static final String HTTPS_LINK = "https://mcgolem.com/services/banner?=";

    public static String serialize(String code) {
        return "https://mcgolem.com/services/banner?=" + code;
    }

    public static String deserialize(String link) {

        if (link.startsWith(HTTP_LINK) || link.startsWith(HTTPS_LINK)) {
            return link.replaceFirst("^[^_]*=", "");
        }

        return link;

    }

}
