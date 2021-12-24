package hikko.betterinteraction;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import hikko.betterinteraction.authme.authEvents;
import hikko.betterinteraction.clearEntities.CheckerEntities;
import hikko.betterinteraction.commands.Commands;
import hikko.betterinteraction.customChat.ChatEvents;
import hikko.betterinteraction.itemLogger.itemEvents;
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

public final class BetterInteraction extends JavaPlugin {

    private static BetterInteraction instance;
    public static String version;
    public static String prefix;
    private static ProtocolManager protocolManager;
    private static ChatEvents chatEvents;
    private static Properties properties;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        File file = new File(BetterInteraction.getInstance().getDataFolder() + "/logs/");
        if (!file.exists()) {
            try {
                BetterInteraction.getInstance().getLogger().log(Level.INFO, "Trying to create a directory...");
                Files.createDirectories(Paths.get(BetterInteraction.getInstance().getDataFolder() + "/logs/"));
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
        new Commands();
        Bukkit.getPluginManager().registerEvents(new itemEvents(), this);
        Bukkit.getPluginManager().registerEvents(new authEvents(), this);
        Bukkit.getPluginManager().registerEvents(chatEvents = new ChatEvents(), this);

        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Successfully enabled.");
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Author: Hikk0o (https://github.com/Hikk0o)");
    }

    @Override
    public void onDisable() {
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Goodbye!");
    }

    public void onReload() {
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Reload plugin...");
        saveDefaultConfig();
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Reloaded.");
    }

    public static BetterInteraction getInstance() {
        return instance;
    }
    public static ProtocolManager getProtocolManager() {
        return protocolManager;
    }
    public static Properties getProperties() {
        return properties;
    }
    public ChatEvents getChatEvents() {
        return chatEvents;
    }
    public static IEssentials getAPIEssentials() {
        PluginManager manager = Bukkit.getPluginManager();
        Plugin plugin = manager.getPlugin("Essentials");
        return (IEssentials) plugin;
    }
}
