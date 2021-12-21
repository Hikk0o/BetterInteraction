package hikko.betterperformance.commands;

import com.google.common.collect.Lists;
import hikko.betterperformance.BetterPerformance;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class Commands extends AbstractCommand {

    public Commands() {
        super("bp");
        BetterPerformance.getInstance().getLogger().log(Level.INFO, "Loading commands...");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        String noPermission = ChatColor.RED + "{noPermission}";
        noPermission = noPermission.replace("{noPermission}", Objects.requireNonNull(BetterPerformance.getInstance().getConfig().getString("messages.noPermission")));
        noPermission = noPermission.replace("\\", "");


        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + BetterPerformance.getInstance().getName() + " " + BetterPerformance.version + " by Hikk0o");
            return;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission(BetterPerformance.getPermissions().reload)) {
                BetterPerformance.getInstance().onReload();
                if (!sender.getName().equals("CONSOLE")) {
                    sender.sendMessage(ChatColor.YELLOW + BetterPerformance.prefix + " Reloaded.");
                }
            } else {
                sender.sendMessage(noPermission);
            }
            return;
        }
        if (args[0].equalsIgnoreCase("detelemessage")) {
            if (sender.hasPermission(BetterPerformance.getPermissions().detelemessage)) {
                BetterPerformance.getInstance().getChatEvents().delMessage(Integer.parseInt(args[1]));
            } else {
                sender.sendMessage(noPermission);
            }
            return;
        }

        sender.sendMessage(ChatColor.GRAY + BetterPerformance.prefix + " Неизвестная команда: " + args[0]);

    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("reload");
        return Lists.newArrayList();
    }
}
