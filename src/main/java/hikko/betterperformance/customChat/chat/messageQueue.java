package hikko.betterperformance.customChat.chat;

import org.bukkit.entity.Player;

import java.awt.*;
import java.util.ArrayList;

public class messageQueue {

    ArrayList<PlayerMessages> queuePlayersMessages = new ArrayList<>();

    public void addPlayer(Player player) {
        queuePlayersMessages.add(new PlayerMessages(player.getName()));
    }

    public PlayerMessages getPlayer (Player player) {
        for (PlayerMessages queuePlayersMessage : queuePlayersMessages) {
            if (queuePlayersMessage.getPlayer().equals(player.getName())) {
                return queuePlayersMessage;
            }
        }
        return null;
    }
}
