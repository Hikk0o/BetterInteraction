package hikko.loggeritems;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;

import java.util.logging.Level;


public class Permissions {
    public static Permission reload = new Permission("logger.reload");
    public Permissions() {
        LoggerItems.getInstance().getLogger().log(Level.INFO, "Loading permissions...");
        Bukkit.getPluginManager().addPermission(reload);
    }
}
