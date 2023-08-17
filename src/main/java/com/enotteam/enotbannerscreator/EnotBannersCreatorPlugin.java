package com.enotteam.enotbannerscreator;

import com.enotteam.enotbannerscreator.banner.crafting.BannerCraftingManager;
import com.enotteam.enotbannerscreator.banner.importing.BannerImportManager;
import com.enotteam.enotbannerscreator.banner.rotating.BannerRotateManager;
import com.enotteam.enotbannerscreator.command.MainCommand;
import com.enotteam.enotbannerscreator.command.RotateCommand;
import com.enotteam.enotbannerscreator.common.Lang;
import com.enotteam.enotbannerscreator.common.Messages;
import com.enotteam.enotbannerscreator.database.ConnectionGenerator;
import com.enotteam.enotbannerscreator.database.DatabaseManager;
import com.enotteam.enotbannerscreator.database.cache.PlayerCache;
import com.enotteam.enotbannerscreator.economy.EconomyManager;
import com.enotteam.enotbannerscreator.economy.PlayerPointsEconomyManager;
import com.enotteam.enotbannerscreator.economy.StubEconomyManager;
import com.enotteam.enotbannerscreator.economy.VaultEconomyManager;
import com.enotteam.enotbannerscreator.listener.AsyncPlayerChatListener;
import com.enotteam.enotbannerscreator.listener.PlayerJoinListener;
import com.enotteam.enotbannerscreator.listener.PlayerQuitListener;
import com.enotteam.enotbannerscreator.menu.*;
import com.enotteam.enotbannerscreator.menu.component.MenuFiller;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class EnotBannersCreatorPlugin extends JavaPlugin {

    BukkitAudiences audiences;
    DatabaseManager databaseManager;
    EconomyManager economyManager;

    @Override
    public void onEnable() {

        createConfig();

        createLangFiles();
        Lang.initialize(this, getConfig().getString("lang"));

        audiences = BukkitAudiences.builder(this).build();
        Messages.initialize(audiences);

        int maxBannerValueLength = getConfig().getInt("banners.max-value-length");

        /*
         * Database
         */
        try {
            databaseManager = new DatabaseManager(
                    new ConnectionGenerator(getDataFolder(), getConfig()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        PlayerCache playerCache = new PlayerCache(databaseManager);

        getServer().getOnlinePlayers().forEach(player -> {
            playerCache.load(player.getName());
        });

        /*
         * Managers
         */
        economyManager = getEconomyManager();
        BannerCraftingManager bannerCraftingManager = new BannerCraftingManager(getConfig().getConfigurationSection("banners.crafting"));
        BannerImportManager bannerImportManager = new BannerImportManager(this);

        /*
         * Menus
         */
        MenuFiller menuFiller = new MenuFiller(getConfig().getConfigurationSection("gui"));

        PatternSelectMenu patternSelectMenu = new PatternSelectMenu(maxBannerValueLength, playerCache);
        BaseColorSelectMenu baseColorSelectMenu = new BaseColorSelectMenu(menuFiller);
        BannerInfoMenu bannerInfoMenu = new BannerInfoMenu(menuFiller, bannerCraftingManager, economyManager, playerCache);
        PremadeBannersMenu premadeBannersMenu = new PremadeBannersMenu(menuFiller, getConfig().getStringList("banners.premade.values"));
        MainMenu mainMenu = new MainMenu(menuFiller, bannerImportManager, baseColorSelectMenu, playerCache);

        StaticMenus.set(mainMenu, bannerInfoMenu, patternSelectMenu, baseColorSelectMenu, premadeBannersMenu);

        /*
         * Listeners
         */
        new PlayerJoinListener(playerCache).register(this);
        new PlayerQuitListener(playerCache).register(this);
        new AsyncPlayerChatListener(maxBannerValueLength, bannerImportManager, playerCache).register(this);

        /*
         * Commands
         */
        new MainCommand(
                getConfig().getString("banners.command.label", "enotbannerscreator"),
                getConfig().getStringList("banners.command.aliases"),
                getConfig().getBoolean("banners.command.permission.need", true),
                getConfig().getString("banners.command.permission.value", "enotbannerscreator.use")).register();

        if (getConfig().getBoolean("banners.rotatable.enabled")) {
            BannerRotateManager bannerRotateManager = new BannerRotateManager(
                    this, getConfig().getConfigurationSection("banners.rotatable"));
            new RotateCommand(
                    getConfig().getString("banners.rotatable.command.label"),
                    getConfig().getStringList("banners.rotatable.command.aliases"),
                    bannerRotateManager,
                    getConfig().getString("banners.rotatable.permission.use")).register();
        }

    }

    @Override
    public void onDisable() {

        this.audiences.close();
        this.audiences = null;

    }

    private void createConfig() {
        File config = new File(getDataFolder() + File.pathSeparator + "config.yml");
        if(!config.exists()) {
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
        reloadConfig();
    }

    private void createLangFiles() {
        File folder = new File(getDataFolder(), "lang");
        if (folder.mkdir()) {
            saveResource("lang/ru_RU.yml", false);
            saveResource("lang/en_GB.yml", false);
        }
    }

    private EconomyManager getEconomyManager() {

        if (!getConfig().getBoolean("economy.enabled", false)) {
            return new StubEconomyManager();
        }

        String economyType = getConfig().getString("economy.type", "vault").toLowerCase();

        if (economyType.equals("vault")) {
            if (!getServer().getPluginManager().isPluginEnabled("Vault")) {
                getLogger().warning("You have selected the Vault economy, but it is not found on the server!");
                return new StubEconomyManager();
            }
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                getLogger().warning("You have selected the Vault economy, but it is not found on the server!");
                return new StubEconomyManager();
            }
            return new VaultEconomyManager(rsp.getProvider());
        }

        if (economyType.equals("playerpoints")) {

            if (!getServer().getPluginManager().isPluginEnabled("PlayerPoints")) {
                getLogger().warning("You have selected the PlayerPoints economy, but it is not found on the server!");
                return new StubEconomyManager();
            }

            return new PlayerPointsEconomyManager();

        }

        return new StubEconomyManager();

    }

}
