package hikko.betterperformance.authme;

import fr.xephi.authme.api.v3.AuthMeApi;
import fr.xephi.authme.events.LoginEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class authEvents implements Listener {

    @EventHandler
    public void LoginEvent(LoginEvent e) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.canSee(e.getPlayer())) {
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, (float) 0.3, 1);
                player.sendMessage(ChatColor.GREEN + "[+] " + ChatColor.YELLOW + e.getPlayer().getName());
            }
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent e) {
        if (AuthMeApi.getInstance().isAuthenticated(e.getPlayer())) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.canSee(e.getPlayer())) {
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, (float) 0.3, (float) 0.2);
                    player.sendMessage(ChatColor.RED + "[-] " + ChatColor.YELLOW + e.getPlayer().getName());
                }
            }
        }
    }
}
