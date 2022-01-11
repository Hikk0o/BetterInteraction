package hikko.betterinteraction.customChat.chat;

import hikko.betterinteraction.BetterInteraction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.logging.Level;


public class PlayerMessages {

    private final String player;
    private boolean cooldown = false;
    private String lastSendMessage = "";
    private boolean lock = false;
    private final ArrayList<Component> messages = new ArrayList<>();

    PlayerMessages(String player) {
        this.player = player;
    }

    public String getLastSendMessage() {
        return lastSendMessage;
    }

    public void setLastSendMessage(String lastSendMessage) {
        this.lastSendMessage = lastSendMessage;
    }

    public boolean isCooldown() {
        return cooldown;
    }

    public void setCooldown(boolean cooldown) {
        this.cooldown = cooldown;
    }

    public synchronized void addMessage(Component message) {
        try {
            this.messages.add(message);
            if (this.messages.size() > 100) {
                this.messages.remove(0);
            }
//            BetterPerformance.getInstance().getLogger().log(Level.WARNING, "Add message for " + this.player + ": " + PlainTextComponentSerializer.plainText().serialize(message));
        } catch (Exception e) {
            e.printStackTrace();
            BetterInteraction.getInstance().getLogger().log(Level.WARNING, "this.messages.size() "+ this.messages.size());
            BetterInteraction.getInstance().getLogger().log(Level.WARNING, "message.toString() "+ message.toString());
        }
    }

    public synchronized void delMessage(Component component, String adminNickname) {
        boolean equals = false;
        for (int a = 0; a < this.messages.size(); a++) {
            Component chatType = null;
            String playerMessage;
            String delMessage;

            try {
//                int counter = 0;

                if (PlainTextComponentSerializer.plainText().serialize(this.messages.get(a).children().get(0)).equals("[X] ")) {
                    for (int counter = 1; counter < this.messages.get(a).children().size(); counter++) {
                        if (PlainTextComponentSerializer.plainText().serialize(this.messages.get(a).children().get(counter)).equals("[L] ") ||
                                PlainTextComponentSerializer.plainText().serialize(this.messages.get(a).children().get(counter)).equals("[G] ")) {
                                chatType = this.messages.get(a).children().get(counter);
                                break;
                        }
                    }
                } else {
                    for (int counter = 0; counter < this.messages.get(a).children().size(); counter++) {
                    if (PlainTextComponentSerializer.plainText().serialize(this.messages.get(a).children().get(counter)).equals("[L] ") ||
                            PlainTextComponentSerializer.plainText().serialize(this.messages.get(a).children().get(counter)).equals("[G] ")) {
                        chatType = this.messages.get(a).children().get(counter);
                        break;
                    }
                }

                }


//                if (PlainTextComponentSerializer.plainText().serialize(this.messages.get(a).children().get(0)).equals("[X] ")) {
//                    if (PlainTextComponentSerializer.plainText().serialize(this.messages.get(a).children().get(1)).equals("[L] ") ||
//                            PlainTextComponentSerializer.plainText().serialize(this.messages.get(a).children().get(1)).equals("[G] ")) {
//                        if ()
//                        chatType = this.messages.get(a).children().get(1);
//                    }
//                } else if (PlainTextComponentSerializer.plainText().serialize(this.messages.get(a).children().get(0)).equals("[L] ") ||
//                        PlainTextComponentSerializer.plainText().serialize(this.messages.get(a).children().get(0)).equals("[G] ")) {
//                    chatType = this.messages.get(a).children().get(0);
//                }

                playerMessage = PlainTextComponentSerializer.plainText().serialize(this.messages.get(a));
                delMessage = PlainTextComponentSerializer.plainText().serialize(component);

            } catch (IndexOutOfBoundsException e) {
                continue;
            }

            playerMessage = playerMessage
                    .replace("[X] ", "")
                    .replace("[L] ", "")
                    .replace("[G] ", "")
                    .replace("✦ ", "")
                    .replace("\uD83D\uDEE1 ", "")
                    .replace(ChatColor.AQUA + "", "")
                    .replace(ChatColor.RESET + "", "");

            delMessage = delMessage
                    .replace("✦ ", "")
                    .replace("\uD83D\uDEE1 ", "");

//            BetterInteraction.getInstance().getLogger().log(Level.WARNING, playerMessage);
//            BetterInteraction.getInstance().getLogger().log(Level.WARNING, delMessage);
            if (playerMessage.equals(delMessage)) {

                equals = true;
                Component deteledMessage = Component.empty();
                if (chatType != null) {
                    deteledMessage = deteledMessage.append(chatType);
                }

                StringBuilder nickname = new StringBuilder();
                int counter = 0;
                while (playerMessage.charAt(counter) != ' ') {
                    nickname.append(playerMessage.charAt(counter));
                    counter++;
                }

                deteledMessage = deteledMessage.append(Component.text(nickname + " "))
                        .color(TextColor.color(0x7C7977));
                deteledMessage = deteledMessage.append(Component.text( ChatColor.ITALIC + "* удалено администратором "+ adminNickname +" *")
                        .color(TextColor.color(0x52504F)));

                this.messages.set(a, deteledMessage);
            }
        }
        if (equals) {
            Player player = BetterInteraction.getInstance().getServer().getPlayer(getPlayer());
            lock = true;
            for (int a = 0; a< 100; a++) {
                assert player != null;
                player.sendMessage("\n");
            }
            for (Component value : this.messages) {
                player.sendMessage(value);
//            BetterPerformance.getInstance().getLogger().log(Level.WARNING, "Send comp");
            }
            lock = false;
        }
    }

    public synchronized String getPlayer() {
        return this.player;
    }

    public synchronized boolean isLock() {
        return this.lock;
    }
}
