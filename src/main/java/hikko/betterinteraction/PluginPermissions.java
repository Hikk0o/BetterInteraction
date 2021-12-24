package hikko.betterinteraction;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.logging.Level;

public class PluginPermissions {

    public final Permission reload = new Permission("betterinteraction.reload");
    public final Permission detelemessage = new Permission("betterinteraction.detelemessage");

    public PluginPermissions() {
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Loading permissions...");
        Bukkit.getPluginManager().addPermission(reload);
        Bukkit.getPluginManager().addPermission(detelemessage);
    }

    public PluginPermissions getPermissions() {
        return this;
    }
}
