package hikko.betterinteraction.donateSystem;

import fr.xephi.authme.events.LoginEvent;
import hikko.betterinteraction.BetterInteraction;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
    public void OnLogoutEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        database.getDonatePlayers().removeIf(donatePlayer -> donatePlayer.getName().equals(player.getName()));
    }

    @EventHandler
    public void ClickEvent(InventoryClickEvent e) {

        if (PlainTextComponentSerializer.plainText().serialize(e.getView().title()).equals("Donate Menu")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {

                String nameItem = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(e.getCurrentItem().getItemMeta().displayName()));
                if (nameItem.equals("Закрыть меню")) {
                    e.getView().close();
                }
                if (nameItem.equals("Список услуг")) {
                    Player player = BetterInteraction.getInstance().getServer().getPlayer(e.getWhoClicked().getName());
                    donatePages.DonateListMenu(player);
                }
            }
        }

        if (PlainTextComponentSerializer.plainText().serialize(e.getView().title()).equals("Список услуг")) {
            e.setCancelled(true);
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
                if (e.isRightClick()) return;
                String nameItem = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(e.getCurrentItem().getItemMeta().displayName()));
                if (nameItem.equals("Закрыть меню")) {
                    e.getView().close();
                }
                if (nameItem.equals("Сменить цвет ника в чате")) {
                    int changeNicknameCost = BetterInteraction.getInstance().getConfig().getInt("prices.coloredNickname.cost");

                    e.getView().close();
                    Player player = BetterInteraction.getInstance().getServer().getPlayer(e.getWhoClicked().getName());
                    if (player == null) return;
                    if (BetterInteraction.getInstance().getDonateDatabase().getPlayer(player.getName()).isColoredNickname()) {
                        player.sendMessage(ChatColor.YELLOW + "У вас уже есть эта услуга.");
                        return;
                    }
                    Database database = BetterInteraction.getInstance().getDonateDatabase();
                    if (database.getDonate(player.getName()) == null) return;
                    int playerBalance = (int) database.getDonate(player.getName());
                    if (playerBalance >= changeNicknameCost) {
                        int newPlayerBalance = playerBalance-changeNicknameCost;
                        if (database.addPlayerPurchase(player.getName(), "coloredNickname")) {
                            database.setDonate(player.getName(), newPlayerBalance);
                        } else {
                            player.sendMessage(ChatColor.RED + "Произошла внутренняя ошибка сервера.");
                            return;
                        }
                        player.sendMessage(ChatColor.GREEN + "Успешная покупка! Баланс: " + newPlayerBalance);
                    } else {
                        player.sendMessage(ChatColor.RED + "Недостаточно донат-коинов.");
                    }

                }
            }
        }
    }


}
