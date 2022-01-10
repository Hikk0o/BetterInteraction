package hikko.betterinteraction.customChat.chat;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MessageQueue {

    final ArrayList<PlayerMessages> queuePlayersMessages = new ArrayList<>();

    public synchronized void addPlayer(Player player) {
        queuePlayersMessages.add(new PlayerMessages(player.getName()));
    }

    public synchronized void delPlayer(Player player) {
        queuePlayersMessages.removeIf(playerMessages -> playerMessages.getPlayer().equals(player.getName()));
    }

    public synchronized PlayerMessages getPlayer (Player player) {
        for (PlayerMessages queuePlayerMessages : queuePlayersMessages) {
            if (queuePlayerMessages.getPlayer().equals(player.getName())) {
//                BetterPerformance.getInstance().getLogger().log(Level.WARNING, queuePlayerMessages.getPlayer());
                return queuePlayerMessages;
            }
        }
        return null;
    }

    public synchronized void delMessage (Component component, String nickname) {
        for (PlayerMessages playerMessages : queuePlayersMessages) {
//            BetterPerformance.getInstance().getLogger().log(Level.WARNING, "Del for " + playerMessages.getPlayer());
            playerMessages.delMessage(component, nickname);
        }
    }
}
