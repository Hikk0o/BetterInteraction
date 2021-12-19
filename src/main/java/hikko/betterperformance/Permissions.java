package hikko.betterperformance;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.logging.Level;

public class Permissions {

    final private Permission reload = new Permission("logger.reload");

    public Permissions() {
        BetterPerformance.getInstance().getLogger().log(Level.INFO, "Loading permissions...");
        Bukkit.getPluginManager().addPermission(reload);

    }

    public Permission getPermissionReload() {
        return reload;
    }
}
