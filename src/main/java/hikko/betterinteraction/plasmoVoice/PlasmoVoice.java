package hikko.betterinteraction.plasmoVoice;

import hikko.betterinteraction.BetterInteraction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import su.plo.voice.PlasmoVoiceAPI;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PlasmoVoice implements Listener {

    private PlasmoVoiceAPI voiceAPI;

    public PlasmoVoice() {
        RegisteredServiceProvider<PlasmoVoiceAPI> provider = Bukkit.getServicesManager().getRegistration(PlasmoVoiceAPI.class);

        if (provider != null) {
            voiceAPI = provider.getProvider();
        }
    }

    public PlasmoVoiceAPI getVoiceAPI() {
        return voiceAPI;
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Logger logger = BetterInteraction.getInstance().getLogger();
        if (player.hasPermission("canSpeak")) {
            voiceAPI.unmute(player.getUniqueId(), true);
            logger.log(Level.INFO, player.getName() + " has permission for voice chat.");
        } else {
            voiceAPI.mute(e.getPlayer().getUniqueId(), 9999L, PlasmoVoiceAPI.DurationUnit.DAYS, "Нет доступа", true);
            logger.log(Level.INFO, player.getName() + " does not have permission for voice chat.");
        }
    }
}
