package hikko.betterperformance;

import hikko.betterperformance.ClearEntities.CheckerEntities;
import hikko.betterperformance.commands.Commands;
import hikko.betterperformance.itemLogger.events;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;

public final class BetterPerformance extends JavaPlugin {

    private static BetterPerformance instance;
    private static PluginPermissions pluginPermissions;
    public static String version;
    public static String prefix;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        File file = new File(BetterPerformance.getInstance().getDataFolder() + "/logs/");
        if (!file.exists()) {
            try {
                BetterPerformance.getInstance().getLogger().log(Level.INFO, "Trying to create a directory...");
                Files.createDirectories(Paths.get(BetterPerformance.getInstance().getDataFolder() + "/logs/"));
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
        new Commands();

        Bukkit.getPluginManager().registerEvents(new events(), this);

        BetterPerformance.getInstance().getLogger().log(Level.INFO, "Successfully enabled.");
        BetterPerformance.getInstance().getLogger().log(Level.INFO, "Author: Hikk0o (https://github.com/Hikk0o)");
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        BetterPerformance.getInstance().getLogger().log(Level.INFO, "Goodbye!");
        // Plugin shutdown logic
    }

    public static PluginPermissions getPermissions() {
        return pluginPermissions.getPermissions();
    }

    public void onReload() {
        BetterPerformance.getInstance().getLogger().log(Level.INFO, "Reload plugin...");
        saveDefaultConfig();
        BetterPerformance.getInstance().getLogger().log(Level.INFO, "Reloaded.");
    }

    public static BetterPerformance getInstance() {
        return instance;
    }
}