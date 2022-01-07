package hikko.betterinteraction.donateSystem;

import fr.xephi.authme.events.LoginEvent;
import hikko.betterinteraction.BetterInteraction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;
import java.util.logging.Level;

public class DonateSystemEvents implements Listener {

    @EventHandler
    public void OnLoginEvent(LoginEvent e) {
        BetterInteraction.getInstance().getDonateDatabase().addPlayer(e.getPlayer());
    }

    @EventHandler
    public void ClickEvent(InventoryClickEvent e) {

        if (PlainTextComponentSerializer.plainText().serialize(e.getView().title()).equals("Donate Menu")) {

            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {

                String nameItem = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(e.getCurrentItem().getItemMeta().displayName()));
                if (nameItem.equals("Закрыть меню")) {
                    BetterInteraction.getInstance().getLogger().log(Level.INFO, "Closed");
                    e.getView().close();
                }
            }

            e.setCancelled(true);
        }
    }


}
