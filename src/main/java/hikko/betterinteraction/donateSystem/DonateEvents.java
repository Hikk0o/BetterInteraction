package hikko.betterinteraction.donateSystem;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.LogoutEvent;
import hikko.betterinteraction.BetterInteraction;
import hikko.betterinteraction.Database;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;
import java.util.Objects;
import java.util.logging.Level;

public class DonateEvents implements Listener {

    DonatePages donatePages;
    Database database;
    Configuration config;

    public DonateEvents() {
        donatePages = new DonatePages();
        database = BetterInteraction.getInstance().getDatabase();
        config = BetterInteraction.getInstance().getConfig();
    }

    public DonatePages getDonatePages() {
        return donatePages;
    }

    @EventHandler
    public void LoginEvent(LoginEvent e) { // AuthMe
        Player player = e.getPlayer();
        database.addPlayer(player);
    }

    @EventHandler
    public void LogoutEvent(LogoutEvent e) { // AuthMe
        Player player = e.getPlayer();
        database.getDonatePlayers().removeIf(donatePlayer -> donatePlayer.getName().equals(player.getName()));
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        database.getDonatePlayers().removeIf(donatePlayer -> donatePlayer.getName().equals(player.getName()));
    }

    private void successfulBuy(Player player, int balance) {
        Title title = Title.title(Component.text("???????????????? ??????????????!").color(TextColor.color(0x55FF55)), Component.text("?????????????? ???? ?????????????????? ?????????????? ???").color(TextColor.color(0xFFFF55)), Title.Times.of(Duration.ofMillis(500), Duration.ofSeconds(4), Duration.ofSeconds(1)));
        Location location = player.getLocation();
        location.add(0, 1.5, 0);

        player.sendMessage(ChatColor.GREEN + "???????????????? ??????????????! ????????????: " + ChatColor.YELLOW + balance);
        player.spawnParticle(Particle.FIREWORKS_SPARK, location, 1000);
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, (float) 0.7, (float) 1);
        player.showTitle(title);
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
                if (nameItem.equals("?????????????? ????????")) {
                    e.getView().close();
                }
                if (nameItem.equals("???????????? ??????????")) {
                    donatePages.DonateListMenu(player);
                }
                if (nameItem.equals("?????????????????? ?????? ????????????????????????")) {
                    donatePages.aboutDonate(player);
                }
            }
        }

        if (menuTitle.equals("???????????? ??????????")) {
            e.setCancelled(true);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, (float) 0.7, 1);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                if (e.isRightClick()) return;
                String nameItem = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(e.getCurrentItem().getItemMeta().displayName()));
                if (nameItem.equals("?????????????? ????????")) {
                    e.getView().close();
                }
                if (nameItem.equals("?????????????? ??????")) {
                    donatePages.ConfirmPage(player, "coloredNickname");
                }
                if (nameItem.equals("??????????????")) {
                    donatePages.ConfirmPage(player, "sponsor");
                }
                if (nameItem.equals("???????? ????????????????")) {
                    donatePages.ConfirmPage(player, "particleMenu");
                }
                if (nameItem.equals("+50 ????????")) {
                    donatePages.ConfirmPage(player, "addPower");
                }
            }
        }

        String productTitle = "??????????: ";

        if (menuTitle.equals(productTitle+"+50 ????????")) {
            e.setCancelled(true);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, (float) 0.7, 1);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                if (e.isRightClick()) return;
                String nameItem = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(e.getCurrentItem().getItemMeta().displayName()));
                if (nameItem.equals("?????????????????? ??????????????")) {
                    e.getView().close();
                }
                if (nameItem.equals("?????????????????????? ??????????????")) {

                    int productCost = config.getInt("prices.addPower.cost");
                    e.getView().close();
                    if (database.getDonate(player.getName()) == null) {
                        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Null");
                    }
                    int playerBalance = (int) database.getDonate(player.getName());
                    int newPlayerBalance = playerBalance - productCost;
                    if (playerBalance >= productCost) {
                        database.setDonate(player.getName(), newPlayerBalance);

                        FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
                        double boost = fplayer.getPowerBoost();
                        fplayer.setPowerBoost(boost+50.0);
                        fplayer.alterPower(50.0);

                        successfulBuy(player, newPlayerBalance);
                    } else {
                        player.sendMessage(ChatColor.RED + "???????????????????????? ??????????-????????????. ????????????: " + ChatColor.YELLOW + playerBalance);
                    }
                }
            }
        }

        if (menuTitle.equals(productTitle+"??????????????") || menuTitle.equals(productTitle+"?????????????? ??????") || menuTitle.equals(productTitle+"???????? ????????????????")) {
            e.setCancelled(true);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, (float) 0.7, 1);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                if (e.isRightClick()) return;
                String nameItem = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(e.getCurrentItem().getItemMeta().displayName()));
                if (nameItem.equals("?????????????????? ??????????????")) {
                    e.getView().close();
                }
                if (nameItem.equals("?????????????????????? ??????????????")) {

                    String product = null;

                    if (menuTitle.equals(productTitle+"??????????????")) product = "sponsor";
                    if (menuTitle.equals(productTitle+"?????????????? ??????")) product = "coloredNickname";
                    if (menuTitle.equals(productTitle+"???????? ????????????????")) product = "particleMenu";

                    int productCost = config.getInt("prices."+product+".cost");

                    e.getView().close();

                    if (database.getDonate(player.getName()) == null) return;

                    if (product.equals("coloredNickname")) {
                        if (database.getPlayer(player.getName()).isColoredNickname()) {
                            player.sendMessage(ChatColor.YELLOW + "?? ?????? ?????? ???????? ?????? ????????????.");
                            return;
                        }
                    }
                    if (product.equals("sponsor")) {
                        if (database.getPlayer(player.getName()).isSponsor()) {
                            player.sendMessage(ChatColor.YELLOW + "?? ?????? ?????? ???????? ?????? ????????????.");
                            return;
                        }
                    }
                    if (product.equals("particleMenu")) {
                        if (database.getPlayer(player.getName()).isHaveEffects()) {
                            player.sendMessage(ChatColor.YELLOW + "?? ?????? ?????? ???????? ?????? ????????????.");
                            return;
                        }
                    }


                    int playerBalance = (int) database.getDonate(player.getName());
                    if (playerBalance >= productCost) {
                        int newPlayerBalance = playerBalance - productCost;
                        if (database.addPlayerPurchase(player.getName(), product)) {
                            database.setDonate(player.getName(), newPlayerBalance);
                        } else {
                            player.sendMessage(ChatColor.RED + "?????????????????? ???????????????????? ???????????? ??????????????.");
                            return;
                        }

                        successfulBuy(player, newPlayerBalance);
                    } else {
                        player.sendMessage(ChatColor.RED + "???????????????????????? ??????????-????????????. ????????????: " + ChatColor.YELLOW + playerBalance);
                    }
                }
            }
        }
    }


}
