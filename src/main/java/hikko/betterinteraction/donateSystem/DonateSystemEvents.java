package hikko.betterinteraction.donateSystem;

import fr.xephi.authme.events.LoginEvent;
import hikko.betterinteraction.BetterInteraction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DonateSystemEvents implements Listener {

    @EventHandler
    public void onLoginEvent(LoginEvent e) {
        BetterInteraction.getInstance().getDonateDatabase().addPlayer(e.getPlayer());
    }

}
