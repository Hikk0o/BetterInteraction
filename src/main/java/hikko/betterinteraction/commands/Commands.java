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

import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class Commands extends AbstractCommand {

    public Commands() {
        super("betterinteraction");
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Loading commands...");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        String noPermission = ChatColor.RED + "{noPermission}";
        noPermission = noPermission.replace("{noPermission}", Objects.requireNonNull(BetterInteraction.getInstance().getConfig().getString("messages.noPermission")));
        noPermission = noPermission.replace("\\", "");


        if (args.length == 0) {
            Component message = Component.empty();
            message = message.append(Component.text(BetterInteraction.getInstance().getName() + "-" + BetterInteraction.version).color(TextColor.color(0xFFFF55)).decorate(TextDecoration.BOLD));
            message = message.append(Component.space());
            message = message.append(Component.text("by Hikk0o")
                    .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("GitHub").color(TextColor.color(0xC8C5C3)).decorate(TextDecoration.ITALIC)))
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, BetterInteraction.getInstance().getProperties().getProperty("website")))
            );
            sender.sendMessage(message);
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
        if (args[0].equalsIgnoreCase("getdonate")) {
            if (sender.hasPermission("betterinteraction.donate.get")) {
                Player player = (Player) sender;
                if (args.length > 1) {
                    Object objDonate = BetterInteraction.getInstance().getDonateDatabase().getDonate(args[1]);
                    Component message;
                    if (objDonate != null) {
                        int donate = (int) objDonate;
                        message = Component
                                .text("Игрок ").color(TextColor.color(0xFFDB45))
                                .append(Component.text(args[1]).color(TextColor.color(0xFFFFFF)))
                                .append(Component.text(" имеет "))
                                .append(Component.text(donate).color(TextColor.color(0xFFFFFF)))
                                .append(Component.text(" донат-поинтов."));
                    } else {
                        message = Component
                                .text("Никнейм ").color(TextColor.color(0xFFDB45))
                                .append(Component.text(args[1]).color(TextColor.color(0xFFFFFF)))
                                .append(Component.text(" не найден в базе данных."));
                    }
                    sender.sendMessage(message);
                } else {
                    int donate = (int) BetterInteraction.getInstance().getDonateDatabase().getDonate(player.getName());
                    Component message = Component
                            .text("Вы имеете ").color(TextColor.color(0xFFDB45))
                            .append(Component.text(donate).color(TextColor.color(0xFFFFFF)))
                            .append(Component.text(" донат-поинтов."));
                    sender.sendMessage(message);
                }
            } else {
                sender.sendMessage(noPermission);
            }
            return;
        }
        if (args[0].equalsIgnoreCase("setdonate")) {
            if (sender.hasPermission("betterinteraction.donate.set")) {
                if (args.length == 3) {
                    int donateValue;
                    try {
                        donateValue = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.GRAY + BetterInteraction.prefix + " /bin setdonate <nickname> <donate>");
                        return;
                    }
                    boolean completed = BetterInteraction.getInstance().getDonateDatabase().setDonate(args[1], donateValue);
                    Component message;
                    if (!completed) {
                        message = Component
                                .text("Никнейм ").color(TextColor.color(0xFFDB45))
                                .append(Component.text(args[1]).color(TextColor.color(0xFFFFFF)))
                                .append(Component.text(" не найден в базе данных."));
                    } else {
                        message = Component
                                .text("Игрок ").color(TextColor.color(0xFFDB45))
                                .append(Component.text(args[1]).color(TextColor.color(0xFFFFFF)))
                                .append(Component.text(" теперь имеет "))
                                .append(Component.text(donateValue).color(TextColor.color(0xFFFFFF)))
                                .append(Component.text(" донат-поинтов."));
                    }
                    sender.sendMessage(message);
                } else {
                    sender.sendMessage(ChatColor.GRAY + BetterInteraction.prefix + " /bin setdonate <nickname> <donate>");
                }
            } else {
                sender.sendMessage(noPermission);
            }
            return;
        }

        sender.sendMessage(ChatColor.GRAY + BetterInteraction.prefix + " Неизвестная команда: " + args[0]);

    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length == 1) return Lists.newArrayList("reload", "getdonate", "setdonate");
        return Lists.newArrayList();
    }
}
