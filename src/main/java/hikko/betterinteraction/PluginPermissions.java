package hikko.betterinteraction;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.logging.Level;

public class PluginPermissions {

    public final Permission reload = new Permission("betterperformance.reload");
    public final Permission detelemessage = new Permission("betterperformance.detelemessage");

    public PluginPermissions() {
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Loading permissions...");
        Bukkit.getPluginManager().addPermission(reload);
        Bukkit.getPluginManager().addPermission(detelemessage);
    }

    public PluginPermissions getPermissions() {
        return this;
    }
}
