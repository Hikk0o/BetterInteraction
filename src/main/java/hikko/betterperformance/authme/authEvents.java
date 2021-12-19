package hikko.betterperformance.authme;

import com.earth2me.essentials.Essentials;
import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.LogoutEvent;
import hikko.betterperformance.BetterPerformance;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Level;

public class authEvents implements Listener {

    @EventHandler
    public void LoginEvent(LoginEvent e) {

        BetterPerformance.getInstance().getLogger().log(Level.INFO, e.getPlayer().getName() + " зашел в аккаунт.");
        BetterPerformance.getInstance().getLogger().log(Level.INFO, String.valueOf(e.getPlayer().canSee(e.getPlayer())));
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, (float) 0.3, 1);
        }
    }

    @EventHandler
    public void LogoutEvent(LogoutEvent e) {
        BetterPerformance.getInstance().getLogger().log(Level.INFO, e.getPlayer().getName() + " вышел из аккаунта.");
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, (float) 0.3, (float) 0.2);
        }
    }
}
