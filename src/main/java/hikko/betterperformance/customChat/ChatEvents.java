package hikko.betterperformance.customChat;

import hikko.betterperformance.BetterPerformance;
import hikko.betterperformance.customChat.chat.MessageQueue;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatEvents implements Listener {
    public ChatEvents() {
        BetterPerformance.getInstance().getLogger().log(Level.INFO, "Loading chat events...");
    }

    MessageQueue messageQueue = new MessageQueue();

    public MessageQueue getMessageQueue() {
        return messageQueue;
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        messageQueue.addPlayer(player);
    }
    @EventHandler
    public void PlayerJoinEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        messageQueue.delPlayer(player);
    }

    public void delMessage(int id) {
        Component message = messages.get(id);
        messageQueue.delMessage(message);
    }

    Logger logger = Logger.getLogger("Chat");
    public static ArrayList<Component> messages = new ArrayList<>();

    int conuter = 0;

    @EventHandler(priority = EventPriority.LOW)
    public void ChatEvent(AsyncChatEvent e) {
        if (e.isCancelled()) return;
        e.setCancelled(true);

        int id = conuter;
        conuter++;

        Component message = Component.text("");

        Component nickname = Component.text(e.getPlayer().getName())
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/w " + e.getPlayer().getName() + " "))
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(ChatColor.GREEN + "Написать личное сообщение\nигроку " + ChatColor.WHITE + e.getPlayer().getName())));

        Component messageColon = Component.text(": ")
                .color(TextColor.color(0x5D5D5D));

        String content = PlainTextComponentSerializer.plainText().serialize(e.message());

        Component global = Component.text("[G] ")
                .color(TextColor.color(0x55FF55));

        Component local = Component.text("[L] ")
                .color(TextColor.color(0xFFFF55));

        Component deleteButton = Component.text("[X] ")
                .color(TextColor.color(0xED4E3F))
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(ChatColor.RED + "Удалить сообщение\n"+ ChatColor.GRAY + ChatColor.ITALIC + "ID: " + id)))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/bp detelemessage " + id));

        Location location = e.getPlayer().getLocation();

        message = message
                .append(nickname)
                .append(messageColon);


        if (content.startsWith("!")) {
            message = message
                    .append(Component.text(content.replaceFirst("!", "")));
            for (Player player : Bukkit.getOnlinePlayers()) {
                Component sendMessage = Component.empty();

                if (player.hasPermission(BetterPerformance.getPermissions().detelemessage)) {
                    sendMessage = sendMessage
                            .append(deleteButton)
                            .append(global)
                            .append(message);
                } else {
                    sendMessage = sendMessage
                            .append(global)
                            .append(message);
                }
                player.sendMessage(sendMessage);
                messageQueue.getPlayer(player).addMessage(sendMessage);

            }

        } else {
            message = message
            .append(Component.text(content));
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!location.getWorld().equals(player.getWorld())) continue;
                if (location.distance(player.getLocation()) < 100) {
                    Component sendMessage = Component.empty();

                    if (player.hasPermission(BetterPerformance.getPermissions().detelemessage)) {
                        sendMessage = sendMessage
                                .append(deleteButton)
                                .append(local)
                                .append(message);
                    } else {
                        sendMessage = sendMessage
                                .append(local)
                                .append(message);
                    }
                    player.sendMessage(sendMessage);
                    messageQueue.getPlayer(player).addMessage(sendMessage);

                }
            }
        }
        messages.add(message);

        logger.log(Level.INFO, PlainTextComponentSerializer.plainText().serialize(message)
                .replaceFirst("\\[G\\] ", ChatColor.GREEN + "[G] " + ChatColor.RESET)
                .replaceFirst("\\[L\\] ", ChatColor.YELLOW + "[L] " + ChatColor.RESET));
    }

}
