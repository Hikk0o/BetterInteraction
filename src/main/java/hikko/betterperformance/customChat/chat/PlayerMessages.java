package hikko.betterperformance.customChat.chat;

import java.awt.*;
import java.util.ArrayList;

public class PlayerMessages {

    private String player;
    private static ArrayList<Component> messages = new ArrayList<>();

    PlayerMessages(String player) {
        this.player = player;
    }

    public void addMessage(Component message) {
        messages.add(message);
    }

    public String getPlayer() {
        return this.player;
    }
}
