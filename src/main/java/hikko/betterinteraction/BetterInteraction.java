package hikko.betterinteraction;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import hikko.betterinteraction.authme.authEvents;
import hikko.betterinteraction.clearEntities.CheckerEntities;
import hikko.betterinteraction.commands.Commands;
import hikko.betterinteraction.customChat.ChatEvents;
import hikko.betterinteraction.customChat.protocol.ChatPacketHandler;
import hikko.betterinteraction.itemLogger.itemEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;

public final class BetterInteraction extends JavaPlugin {

    private static BetterInteraction instance;
    private static PluginPermissions pluginPermissions;
    public static String version;
    public static String prefix;
    private static ProtocolManager protocolManager;
    private static ChatEvents chatEvents;


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

        final Properties properties = new Properties();
        try {
            properties.load(this.getClassLoader().getResourceAsStream("plugin.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        version = properties.getProperty("version");
        prefix = "[" + properties.getProperty("prefix") + "]";

        new CheckerEntities();
        pluginPermissions = new PluginPermissions();
        protocolManager = ProtocolLibrary.getProtocolManager();
        new Commands();
        /*new ChatPacketHandler(this, ListenerPriority.HIGH, PacketType.Play.Server.CHAT);*/
        Bukkit.getPluginManager().registerEvents(new itemEvents(), this);
        Bukkit.getPluginManager().registerEvents(new authEvents(), this);
        Bukkit.getPluginManager().registerEvents(chatEvents = new ChatEvents(), this);
        new ChatPacketHandler();

        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Successfully enabled.");
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Author: Hikk0o (https://github.com/Hikk0o)");
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Goodbye!");
        // Plugin shutdown logic
    }

    public static PluginPermissions getPermissions() {
        return pluginPermissions.getPermissions();
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
    public ChatEvents getChatEvents() {
        return chatEvents;
    }
}
