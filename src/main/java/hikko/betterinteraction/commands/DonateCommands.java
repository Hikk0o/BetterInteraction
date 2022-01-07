package hikko.betterinteraction.commands;

import com.google.common.collect.Lists;
import hikko.betterinteraction.BetterInteraction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public class DonateCommands extends AbstractCommand {

    public DonateCommands() {
        super("donatemenu");
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Loading Faction commands...");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        String noPermission = net.md_5.bungee.api.ChatColor.RED + "{noPermission}";
        noPermission = noPermission.replace("{noPermission}", Objects.requireNonNull(BetterInteraction.getInstance().getConfig().getString("messages.noPermission")));
        noPermission = noPermission.replace("\\", "");

        if (args.length == 0) { // Реализация донат меню
            Player player = (Player) sender;

            Component menuTitle = Component.text("Donate Menu").decoration(ITALIC, false).color(TextColor.color(0x212121));
            Inventory inv = Bukkit.createInventory(player, 27, menuTitle);
            ItemStack empty = new ItemStack(Material.AIR);
            ItemStack balance = new ItemStack(Material.RAW_GOLD_BLOCK);
            ItemStack donateList = new ItemStack(Material.NETHER_STAR);
            ItemStack menuGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
            ItemStack closeMenu = new ItemStack(Material.RED_CONCRETE);

            ItemMeta balanceItemMeta = balance.getItemMeta();
            balanceItemMeta.displayName(Component.text("Баланс").decoration(ITALIC, false).color(TextColor.color(0x6DFE43)));
            List<Component> balanceLore = new ArrayList<>();
            balanceLore.add(Component.text("Вы имеете ").decoration(ITALIC, false).color(TextColor.color(0xF8F8F8))
                    .append(Component.text((int) BetterInteraction.getInstance().getDonateDatabase().getDonate(player.getName())).color(TextColor.color(0xFFDB45)))
                    .append(Component.text(" донат-поинтов.")));
            balanceItemMeta.lore(balanceLore);
            balance.setItemMeta(balanceItemMeta);

            ItemMeta closeMenuItemMeta = closeMenu.getItemMeta();
            closeMenuItemMeta.displayName(Component.text("Закрыть меню").decoration(ITALIC, false).color(TextColor.color(0xFF5A49)));
            closeMenu.setItemMeta(closeMenuItemMeta);

            ItemMeta donateListMeta = donateList.getItemMeta();
            donateListMeta.displayName(Component.text("Список услуг").decoration(ITALIC, false).color(TextColor.color(0xFFFFFF)));
            List<Component> donateListLore = new ArrayList<>();
            donateListLore.add(Component.text("Перейти к списку услуг").decoration(ITALIC, false).color(TextColor.color(0x9C9C9C)));
            donateListMeta.lore(donateListLore);
            donateList.setItemMeta(donateListMeta);

            ItemStack[] items = {
                    menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass,
                    menuGlass, balance, empty, empty, donateList, empty, empty, closeMenu, menuGlass,
                    menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass, menuGlass
            };
            inv.setContents(items);

            player.openInventory(inv);

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
                        sender.sendMessage(net.md_5.bungee.api.ChatColor.GRAY + BetterInteraction.prefix + " /bin setdonate <nickname> <donate>");
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
                    sender.sendMessage(net.md_5.bungee.api.ChatColor.GRAY + BetterInteraction.prefix + " /bin setdonate <nickname> <donate>");
                }
            } else {
                sender.sendMessage(noPermission);
            }
            return;
        }

        sender.sendMessage(net.md_5.bungee.api.ChatColor.GRAY + BetterInteraction.prefix + " Неизвестная команда: " + args[0]);


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
            return list;
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("setdonate") || args[0].equalsIgnoreCase("getdonate"))) {
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
