package hikko.betterinteraction;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import hikko.betterinteraction.authme.AuthEvents;
import hikko.betterinteraction.clearEntities.CheckerEntities;
import hikko.betterinteraction.commands.*;
import hikko.betterinteraction.customChat.ChatEvents;
import hikko.betterinteraction.customRecipe.CustomRecipe;
import hikko.betterinteraction.donateSystem.DonateEvents;
import hikko.betterinteraction.itemLogger.ItemEvents;
import hikko.betterinteraction.plasmoVoice.PlasmoVoice;
import net.ess3.api.IEssentials;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class BetterInteraction extends JavaPlugin {

    private static BetterInteraction instance;
    public static String version;
    public static String prefix;
    private static ProtocolManager protocolManager;
    private static ChatEvents chatEvents;
    private static DonateEvents donateEvents;
    private static ItemEvents itemEvents;
    private static Properties properties;
    private static Database database;
    private Logger logger;
    private PluginManager pluginManager;

    @Override
    public void onEnable() {
        instance = this;
        logger = this.getLogger();
        pluginManager = Bukkit.getPluginManager();

        saveDefaultConfig();

        File file = new File(this.getDataFolder() + "/logs/");
        if (!file.exists()) {
            try {
                Files.createDirectories(Paths.get(this.getDataFolder() + "/logs/"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        file = new File(this.getDataFolder() + "/transactionsLogs/");
        if (!file.exists()) {
            try {
                Files.createDirectories(Paths.get(this.getDataFolder() + "/transactionsLogs/"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        properties = new Properties();
        try {
            properties.load(this.getClassLoader().getResourceAsStream("plugin.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        version = properties.getProperty("version");
        prefix = "[" + properties.getProperty("prefix") + "]";

        new CheckerEntities();
        protocolManager = ProtocolLibrary.getProtocolManager();
        database = new Database();
        database.open();
        new Commands();
        new FactionCommands();
        new EmotesCommands();
        new DonateCommands();
        new ReportCommand();
        pluginManager.registerEvents(new CustomRecipe(), this);
        pluginManager.registerEvents(new PlasmoVoice(), this);
        pluginManager.registerEvents(new AuthEvents(), this);
        pluginManager.registerEvents(itemEvents = new ItemEvents(), this);
        pluginManager.registerEvents(donateEvents = new DonateEvents(), this);
        pluginManager.registerEvents(chatEvents = new ChatEvents(), this);

        logger.log(Level.INFO, "Successfully enabled.");
        logger.log(Level.INFO, "Author: Hikk0o (https://github.com/Hikk0o)");
    }

    @Override
    public void onDisable() {
        logger.log(Level.INFO, "Goodbye!");
        if (database != null) database.close();
    }

    public void onReload() {
        logger.log(Level.INFO, "Reload plugin...");
        reloadConfig();
        itemEvents.updateLogsToConsole();
        chatEvents.updateWords();
        donateEvents.getDonatePages().updateDonateCost();
        logger.log(Level.INFO, "Reloaded.");
    }

    public static BetterInteraction getInstance() {
        return instance;
    }
    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }
    public Properties getProperties() {
        return properties;
    }
    public ChatEvents getChatEvents() {
        return chatEvents;
    }
    public DonateEvents GetDonateSystemEvents() {
        return donateEvents;
    }
    public Database getDatabase() {
        return database;
    }
    public IEssentials getAPIEssentials() {
        Plugin plugin = pluginManager.getPlugin("Essentials");
        return (IEssentials) plugin;
    }
}
