package hikko.loggeritems;

import hikko.loggeritems.commands.Commands;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

public final class LoggerItems extends JavaPlugin {

    private static LoggerItems instance;
    public static String version;
    public static String prefix;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        final Properties properties = new Properties();
        try {
            properties.load(this.getClassLoader().getResourceAsStream("plugin.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        version = properties.getProperty("version");
        prefix = "[" + properties.getProperty("prefix") + "]";

        new Permissions();
        new Commands();

        Bukkit.getPluginManager().registerEvents(new events(), this);

        LoggerItems.getInstance().getLogger().log(Level.INFO, "Successfully enabled.");
        LoggerItems.getInstance().getLogger().log(Level.INFO, "Author: Hikk0o (https://github.com/Hikk0o)");
        // Plugin startup logic

    }

    @Override
    public void onDisable() {

        // Plugin shutdown logic
    }

    public void onReload() {
        LoggerItems.getInstance().getLogger().log(Level.INFO, "Reload plugin...");
        saveDefaultConfig();
        LoggerItems.getInstance().getLogger().log(Level.INFO, "Reloaded.");
    }

    public static LoggerItems getInstance() {
        return instance;
    }
}
