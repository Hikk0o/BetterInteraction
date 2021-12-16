package hikko.loggeritems.commands;

import com.google.common.collect.Lists;
import hikko.loggeritems.LoggerItems;
import hikko.loggeritems.Permissions;
import hikko.loggeritems.events;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class Commands extends AbstractCommand {

    public Commands() {
        super("logger");
        LoggerItems.getInstance().getLogger().log(Level.INFO, "Loading commands...");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        String noPermission = ChatColor.RED + "{noPermission}";
        noPermission = noPermission.replace("{noPermission}", Objects.requireNonNull(LoggerItems.getInstance().getConfig().getString("messages.noPermission")));
        noPermission = noPermission.replace("\\", "");


        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + LoggerItems.getInstance().getName() + " " + LoggerItems.version + " by Hikk0o");
            return;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission(Permissions.reload)) {
                LoggerItems.getInstance().onReload();
                if (!sender.getName().equals("CONSOLE")) {
                    sender.sendMessage(ChatColor.YELLOW + LoggerItems.prefix + " Reloaded.");
                }
            } else {
                sender.sendMessage(noPermission);
            }
            return;
        }

        sender.sendMessage(ChatColor.GRAY + LoggerItems.prefix + " Неизвестная команда: " + args[0]);

    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("reload");
        return Lists.newArrayList();
    }
}
