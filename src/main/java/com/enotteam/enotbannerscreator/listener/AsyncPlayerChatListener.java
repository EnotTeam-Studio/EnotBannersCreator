package com.enotteam.enotbannerscreator.listener;

import com.enotteam.enotbannerscreator.banner.common.BannerParser;
import com.enotteam.enotbannerscreator.banner.importing.BannerImportManager;
import com.enotteam.enotbannerscreator.common.McGolemLinkSerializer;
import com.enotteam.enotbannerscreator.common.Messages;
import com.enotteam.enotbannerscreator.database.cache.PlayerCache;
import com.enotteam.enotbannerscreator.menu.StaticMenus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AsyncPlayerChatListener implements RegistrableListener {

    int maxLength;
    BannerImportManager bannerImportManager;
    PlayerCache playerCache;

    @EventHandler(priority = EventPriority.LOWEST)
    private void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();

        if (!bannerImportManager.isInImportWaiting(player)) return;

        event.setCancelled(true);

        String code = McGolemLinkSerializer.deserialize(event.getMessage());

        if (code.length() > maxLength) {
            Messages.send(player, "import-banner-max-length");
            return;
        }

        if (!BannerParser.isCodeCanBeParsed(code)) {
            Messages.send(player, "import-banner-not-found");
            return;
        }

        bannerImportManager.removeFromImportWaiting(player);
        playerCache.get(player.getName()).getSavedBanners().add(code);
        StaticMenus.getBannerInfoMenu().open(player, code);


    }

}
