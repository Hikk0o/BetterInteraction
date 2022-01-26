package hikko.betterinteraction.commands;

import com.google.common.collect.Lists;
import hikko.betterinteraction.BetterInteraction;
import hikko.betterinteraction.Database;
import hikko.betterinteraction.donateSystem.DonateSystemEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class DonateCommands extends AbstractCommand {

    public DonateCommands() {
        super("donatemenu");
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Loading Faction commands...");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        DonateSystemEvents donateSystemEvents = BetterInteraction.getInstance().GetDonateSystemEvents();
        Database database = BetterInteraction.getInstance().getDatabase();

        String noPermission = ChatColor.RED + "{noPermission}";
        noPermission = noPermission.replace("{noPermission}", Objects.requireNonNull(BetterInteraction.getInstance().getConfig().getString("messages.noPermission")));
        noPermission = noPermission.replace("\\", "");


        if (args.length == 0) { // Реализация донат меню
            if (sender.getName().equals("CONSOLE")) return;
            Player player = (Player) sender;
            donateSystemEvents.getDonatePages().InitialDonateMenu(player);
            return;
        }

        if (args[0].equalsIgnoreCase("getdonate")) {
            if (sender.hasPermission("betterinteraction.donate.get")) {
                Player player = (Player) sender;
                if (args.length > 1) {
                    Object objDonate = database.getDonate(args[1]);
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
                    if (sender.getName().equals("CONSOLE")) return;
                    int donate = (int) database.getDonate(player.getName());
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
                    boolean completed = database.setDonate(args[1], donateValue);
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
        if (args[0].equalsIgnoreCase("adddonate")) {
            if (sender.hasPermission("betterinteraction.donate.add")) {
                if (args.length == 3) {
                    int donateValue;
                    try {
                        donateValue = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.GRAY + BetterInteraction.prefix + " /bin adddonate <nickname> <donate>");
                        return;
                    }

                    Object objectCurrentBalance = database.getDonate(args[1]);
                    if (objectCurrentBalance == null) return;
                    int currentBalance = (int) objectCurrentBalance;

                    boolean completed = database.setDonate(args[1], currentBalance + donateValue);
                    Component message;
                    if (!completed) {
                        message = Component
                                .text("Никнейм ").color(TextColor.color(0xFFDB45))
                                .append(Component.text(args[1]).color(TextColor.color(0xFFFFFF)))
                                .append(Component.text(" не найден в базе данных."));
                    } else {
                        message = Component
                                .text("Игроку ").color(TextColor.color(0xFFDB45))
                                .append(Component.text(args[1]).color(TextColor.color(0xFFFFFF)))
                                .append(Component.text(" добавлено "))
                                .append(Component.text(donateValue).color(TextColor.color(0xFFFFFF)))
                                .append(Component.text(" донат-поинтов.\nБаланс игрока: "))
                                .append(Component.text(currentBalance+donateValue).color(TextColor.color(0xFFFFFF)));
                    }
                    sender.sendMessage(message);

                    Player player = BetterInteraction.getInstance().getServer().getPlayer(args[1]);

                    if (player != null) {
                        int newBalance = currentBalance+donateValue;
                        player.sendMessage(ChatColor.GREEN + "Вам начислено " + ChatColor.YELLOW + donateValue +
                                ChatColor.GREEN + " донат-коинов.\nТекущий баланс: " + ChatColor.YELLOW + newBalance);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, (float) 0.7, (float) 1);
                    }

                } else {
                    sender.sendMessage(ChatColor.GRAY + BetterInteraction.prefix + " /bin adddonate <nickname> <donate>");
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
        ArrayList<String> list = Lists.newArrayList();
        if (args.length == 1) {
            if (sender.hasPermission("betterinteraction.donate.set")) {
                list.add("setdonate");
            }
            if (sender.hasPermission("betterinteraction.donate.get")) {
                list.add("getdonate");
            }
            if (sender.hasPermission("betterinteraction.donate.add")) {
                list.add("adddonate");
            }
            return list;
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("setdonate") || args[0].equalsIgnoreCase("getdonate") || args[0].equalsIgnoreCase("adddonate"))) {
            if (sender.hasPermission("betterinteraction.donate.set") || sender.hasPermission("betterinteraction.donate.get")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    list.add(player.getName());
                }
            }
            return list;
        }
        return Lists.newArrayList();
    }
}
