package hikko.betterinteraction.donateSystem;

import fr.xephi.authme.events.LoginEvent;
import hikko.betterinteraction.BetterInteraction;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class DonateSystemEvents implements Listener {

    DonatePages donatePages;

    public DonateSystemEvents() {
        donatePages = new DonatePages();
    }

    public DonatePages getDonatePages() {
        return donatePages;
    }

    @EventHandler
    public void OnLoginEvent(LoginEvent e) {
        BetterInteraction.getInstance().getDonateDatabase().addPlayer(e.getPlayer());
    }

    @EventHandler
    public void ClickEvent(InventoryClickEvent e) {
        e.setCancelled(true);
        if (PlainTextComponentSerializer.plainText().serialize(e.getView().title()).equals("Donate Menu")) {

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

            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {

                String nameItem = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(e.getCurrentItem().getItemMeta().displayName()));
                if (nameItem.equals("Закрыть меню")) {
                    e.getView().close();
                }
            }
        }
    }


}
