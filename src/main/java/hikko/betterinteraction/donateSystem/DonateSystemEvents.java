package hikko.betterinteraction.donateSystem;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import fr.xephi.authme.events.LoginEvent;
import hikko.betterinteraction.BetterInteraction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;
import java.util.Objects;

public class DonateSystemEvents implements Listener {

    DonatePages donatePages;
    Database database;

    public DonateSystemEvents() {
        donatePages = new DonatePages();
        database = BetterInteraction.getInstance().getDonateDatabase();
    }

    public DonatePages getDonatePages() {
        return donatePages;
    }

    @EventHandler
    public void OnLoginEvent(LoginEvent e) {
        Player player = e.getPlayer();
        database.addPlayer(player);
    }
    @EventHandler
    public void OnLoginEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        CommandSender console = BetterInteraction.getInstance().getServer().getConsoleSender();
        BetterInteraction.getInstance().getServer().dispatchCommand(console, "h clear "+player.getName());
    }

    @EventHandler
    public void OnLogoutEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        database.getDonatePlayers().removeIf(donatePlayer -> donatePlayer.getName().equals(player.getName()));
        if (player.hasPermission("group.donateplayer")) {
            LuckPerms api = LuckPermsProvider.get();
            User user = api.getPlayerAdapter(Player.class).getUser(player);
            DataMutateResult result = user.data().remove(Node.builder("group.donateplayer").build());
            api.getUserManager().saveUser(user);
        }
    }

    @EventHandler
    public void ClickEvent(InventoryClickEvent e) {

        String menuTitle = PlainTextComponentSerializer.plainText().serialize(e.getView().title());
        Player player = BetterInteraction.getInstance().getServer().getPlayer(e.getWhoClicked().getName());
        if (player == null) return;

        if (menuTitle.equals("Donate Menu")) {
            e.setCancelled(true);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, (float) 0.7, 1);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {

                String nameItem = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(e.getCurrentItem().getItemMeta().displayName()));
                if (nameItem.equals("Закрыть меню")) {
                    e.getView().close();
                }
                if (nameItem.equals("Список услуг")) {
                    donatePages.DonateListMenu(player);
                }
            }
        }

        if (menuTitle.equals("Список услуг")) {
            e.setCancelled(true);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, (float) 0.7, 1);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                if (e.isRightClick()) return;
                String nameItem = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(e.getCurrentItem().getItemMeta().displayName()));
                if (nameItem.equals("Закрыть меню")) {
                    e.getView().close();
                }
                if (nameItem.equals("Цветной ник")) {
                    donatePages.ConfirmPage(player, "coloredNickname");
                }
                if (nameItem.equals("Спонсор")) {
                    donatePages.ConfirmPage(player, "sponsor");
                }
                if (nameItem.equals("Меню эффектов")) {
                    donatePages.ConfirmPage(player, "particleMenu");
                }
                if (nameItem.equals("+50 силы для фракции")) {
                    donatePages.ConfirmPage(player, "addPower");
                }
            }
        }

        String productTitle = "Товар: ";

        if (menuTitle.equals(productTitle+"+50 силы для фракции")) {
            e.setCancelled(true);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, (float) 0.7, 1);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                if (e.isRightClick()) return;
                String nameItem = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(e.getCurrentItem().getItemMeta().displayName()));
                if (nameItem.equals("Отклонить покупку")) {
                    e.getView().close();
                }
                if (nameItem.equals("Подтвердить покупку")) {

                    int productCost = BetterInteraction.getInstance().getConfig().getInt("prices.addPower.cost");
                    e.getView().close();
                    if (database.getDonate(player.getName()) == null) return;
                    int playerBalance = (int) database.getDonate(player.getName());
                    int newPlayerBalance = playerBalance - productCost;
                    if (playerBalance >= productCost) {
                        database.setDonate(player.getName(), newPlayerBalance);

                        FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
                        Faction faction = fplayer.getFaction();
                        double boost = faction.getPowerBoost();
                        faction.setPowerBoost(boost+50.0);

                        Title title = Title.title(Component.text("Успешная покупка!").color(TextColor.color(0x55FF55)), Component.empty(), Title.Times.of(Duration.ofMillis(500), Duration.ofSeconds(4), Duration.ofSeconds(1)));
                        Location location = player.getLocation();
                        location.add(0, 1.5, 0);

                        player.sendMessage(ChatColor.GREEN + "Успешная покупка! Баланс: " + ChatColor.YELLOW + newPlayerBalance);
                        player.spawnParticle(Particle.FIREWORKS_SPARK, location, 1000);
                        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, (float) 0.7, (float) 1);
                        player.showTitle(title);
                    }
                }
            }
        }

        if (menuTitle.equals(productTitle+"Спонсор") || menuTitle.equals(productTitle+"Цветной ник") || menuTitle.equals(productTitle+"Меню эффектов")) {
            e.setCancelled(true);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, (float) 0.7, 1);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                if (e.isRightClick()) return;
                String nameItem = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(e.getCurrentItem().getItemMeta().displayName()));
                if (nameItem.equals("Отклонить покупку")) {
                    e.getView().close();
                }
                if (nameItem.equals("Подтвердить покупку")) {

                    String product = null;

                    if (menuTitle.equals(productTitle+"Спонсор")) product = "sponsor";
                    if (menuTitle.equals(productTitle+"Цветной ник")) product = "coloredNickname";
                    if (menuTitle.equals(productTitle+"Меню эффектов")) product = "particleMenu";

                    int productCost = BetterInteraction.getInstance().getConfig().getInt("prices."+product+".cost");

                    e.getView().close();

                    if (database.getDonate(player.getName()) == null) return;

                    if (product.equals("coloredNickname")) {
                        if (database.getPlayer(player.getName()).isColoredNickname()) {
                            player.sendMessage(ChatColor.YELLOW + "У вас уже есть эта услуга.");
                            return;
                        }
                    }
                    if (product.equals("sponsor")) {
                        if (database.getPlayer(player.getName()).isSponsor()) {
                            player.sendMessage(ChatColor.YELLOW + "У вас уже есть эта услуга.");
                            return;
                        }
                    }
                    if (product.equals("particleMenu")) {
                        if (database.getPlayer(player.getName()).isHaveEffects()) {
                            player.sendMessage(ChatColor.YELLOW + "У вас уже есть эта услуга.");
                            return;
                        }
                    }


                    int playerBalance = (int) database.getDonate(player.getName());
                    if (playerBalance >= productCost) {
                        int newPlayerBalance = playerBalance - productCost;
                        if (database.addPlayerPurchase(player.getName(), product)) {
                            database.setDonate(player.getName(), newPlayerBalance);
                        } else {
                            player.sendMessage(ChatColor.RED + "Произошла внутренняя ошибка сервера.");
                            return;
                        }

                        Location location = player.getLocation();
                        location.add(0, 1.5, 0);

                        Title title = Title.title(Component.text("Успешная покупка!").color(TextColor.color(0x55FF55)), Component.empty(), Title.Times.of(Duration.ofMillis(500), Duration.ofSeconds(4), Duration.ofSeconds(1)));

                        player.sendMessage(ChatColor.GREEN + "Успешная покупка! Баланс: " + ChatColor.YELLOW + newPlayerBalance);
                        player.spawnParticle(Particle.FIREWORKS_SPARK, location, 1000);
                        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, (float) 0.7, (float) 1);
                        player.showTitle(title);

                    } else {
                        player.sendMessage(ChatColor.RED + "Недостаточно донат-коинов. Баланс: " + ChatColor.YELLOW + playerBalance);
                    }


                }
            }
        }
    }


}
