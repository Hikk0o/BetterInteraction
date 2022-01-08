package hikko.betterinteraction.commands;

import com.google.common.collect.Lists;
import hikko.betterinteraction.BetterInteraction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class Commands extends AbstractCommand {

    public Commands() {
        super("betterinteraction");
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Loading commands...");
    }

    void sendVersion(CommandSender sender) {
        Component message = Component.empty();
        message = message.append(Component.text(BetterInteraction.getInstance().getName() + "-" + BetterInteraction.version).color(TextColor.color(0xFFFF55)).decorate(TextDecoration.BOLD));
        message = message.append(Component.space());
        message = message.append(Component.text("by Hikk0o")
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("GitHub").color(TextColor.color(0xC8C5C3)).decorate(TextDecoration.ITALIC)))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, BetterInteraction.getInstance().getProperties().getProperty("website")))
        );
        sender.sendMessage(message);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        String noPermission = ChatColor.RED + "{noPermission}";
        noPermission = noPermission.replace("{noPermission}", Objects.requireNonNull(BetterInteraction.getInstance().getConfig().getString("messages.noPermission")));
        noPermission = noPermission.replace("\\", "");


        if (args.length == 0) {
            sendVersion(sender);
            return;
        }

        if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("ver")) {
            sendVersion(sender);
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("betterinteraction.reload")) {
                BetterInteraction.getInstance().onReload();
                if (!sender.getName().equals("CONSOLE")) {
                    sender.sendMessage(ChatColor.YELLOW + BetterInteraction.prefix + " Reloaded.");
                }
            } else {
                sender.sendMessage(noPermission);
            }
            return;
        }
        if (args[0].equalsIgnoreCase("detelemessage")) {
            if (sender.hasPermission("betterinteraction.detelemessage")) {
                BetterInteraction.getInstance().getChatEvents().delMessage(Integer.parseInt(args[1]));
            } else {
                sender.sendMessage(noPermission);
            }
            return;
        }

        sender.sendMessage(ChatColor.GRAY + BetterInteraction.prefix + " Неизвестная команда: " + args[0]);

    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        ArrayList<String> list = Lists.newArrayList();
        if (args.length == 1) {
            list.add("version");
            if (sender.hasPermission("betterinteraction.reload")) {
                list.add("reload");
            }
            if (sender.hasPermission("betterinteraction.detelemessage")) {
                list.add("detelemessage");
            }
        }
        return list;
    }
}
