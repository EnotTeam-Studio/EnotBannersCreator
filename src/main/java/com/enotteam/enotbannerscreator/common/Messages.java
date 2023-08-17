package com.enotteam.enotbannerscreator.common;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class Messages {

    private static BukkitAudiences audiences;

    public static void initialize(BukkitAudiences bukkitAudiences) {
        audiences = bukkitAudiences;
    }

    public static void send(CommandSender sender, String messageId) {
        Audience audience = audiences.sender(sender);
        Colorizer.colorize(Lang.get().getStringList("messages." + messageId)).forEach(audience::sendMessage);
    }

    public static void send(CommandSender sender, String messageId, Map<String, String> placeholders) {
        Audience audience = audiences.sender(sender);
        Colorizer.colorize(PlaceholderReplacer.replacePlaceholders(
                        Lang.get().getStringList("messages." + messageId),
                        placeholders))
                .forEach(audience::sendMessage);
    }

    public static void sendWithLink(CommandSender sender, String messageId, String link) {
        Audience audience = audiences.sender(sender);
        Colorizer.colorize(Lang.get().getStringList("messages." + messageId)).forEach(component ->
                audience.sendMessage(component.clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, link))));
    }

}
