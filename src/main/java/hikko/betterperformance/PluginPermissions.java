package hikko.betterperformance;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.logging.Level;

public class PluginPermissions {

    public Permission reload = new Permission("betterperformance.reload");

    public PluginPermissions() {
        BetterPerformance.getInstance().getLogger().log(Level.INFO, "Loading permissions...");
        Bukkit.getPluginManager().addPermission(reload);
    }

    public PluginPermissions getPermissions() {
        return this;
    }
}
