package hikko.betterinteraction.customChat;

import com.earth2me.essentials.User;
import hikko.betterinteraction.BetterInteraction;
import hikko.betterinteraction.customChat.chat.MessageQueue;
import hikko.betterinteraction.customChat.protocol.ChatPacketHandler;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.ess3.api.events.PrivateMessagePreSendEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
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
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Loading chat events...");
        new ChatPacketHandler();
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

    @EventHandler
    public void PrivateMessageSent(PrivateMessagePreSendEvent e) { // Essentials event
        e.setCancelled(true);
        Player recipient = BetterInteraction.getInstance().getServer().getPlayer(e.getRecipient().getName());
        Player sender = BetterInteraction.getInstance().getServer().getPlayer(e.getSender().getName());
        if (recipient != null && sender != null) {
            recipient.playSound(recipient.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, (float) 0.5, (float) 2);
            Component recipientMessage = Component.empty();
            recipientMessage = recipientMessage
                    .append(Component.text("PM от ").color(TextColor.color(0xFF9D1F)))
                    .append(Component.text(sender.getName())
                            .color(TextColor.color(0xFFDB45))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Ответить игроку ").color(TextColor.color(0xFF9D1F)).append(Component.text(sender.getName()).color(TextColor.color(0xFFDB45)))))
                            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/w " + sender.getName() + " ")))
                    .append(Component.text(": ").color(TextColor.color(0xFF9D1F)))
                    .append(Component.text(e.getMessage()).color(TextColor.color(0xFFFFFF)));

            recipient.sendMessage(recipientMessage);
            messageQueue.getPlayer(recipient).addMessage(recipientMessage);

            sender.playSound(sender.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, (float) 0.3, (float) 1.5);
            Component sernderMessage = Component.empty();
            sernderMessage = sernderMessage
                    .append(Component.text("PM для ").color(TextColor.color(0xFF9D1F)))
                    .append(Component.text(recipient.getName()).color(TextColor.color(0xFFDB45)))
                    .append(Component.text(": ").color(TextColor.color(0xFF9D1F)))
                    .append(Component.text(e.getMessage()).color(TextColor.color(0xFFFFFF)));

            sender.sendMessage(sernderMessage);
            messageQueue.getPlayer(sender).addMessage(sernderMessage);

            Logger.getLogger("PM").log(Level.INFO, "От " + sender.getName() + " для " + recipient.getName() + ": " + e.getMessage());

            User senderr = BetterInteraction.getAPIEssentials().getUser(sender);
            User recipientt = BetterInteraction.getAPIEssentials().getUser(recipient);
            senderr.setReplyRecipient(e.getRecipient());
            recipientt.setReplyRecipient(e.getSender());
        }


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

        Component message = Component.empty();

        Component nickname = Component.text(e.getPlayer().getName())
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/w " + e.getPlayer().getName() + " "))
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(ChatColor.GREEN + "Отправить личное сообщение игроку " + ChatColor.WHITE + e.getPlayer().getName())));

        Component messageColon = Component.text(": ")
                .color(TextColor.color(0x5D5D5D));

        String content = PlainTextComponentSerializer.plainText().serialize(e.message());

        Component global = Component.text("[G] ")
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Глобальный чат").color(TextColor.color(0x55FF55))))
                .color(TextColor.color(0x55FF55));

        Component local = Component.text("[L] ")
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Локальный чат").color(TextColor.color(0xFFFF55))))
                .color(TextColor.color(0xFFFF55));

        Component deleteButton = Component.text("[X] ")
                .color(TextColor.color(0xED4E3F))
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(ChatColor.RED + "Удалить сообщение\n"+ ChatColor.GRAY + ChatColor.ITALIC + "ID: " + id)))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/bi detelemessage " + id));

        Location location = e.getPlayer().getLocation();

        message = message
                .append(nickname)
                .append(messageColon);

        String logMessage;
        if (content.startsWith("!")) {
            logMessage = ChatColor.GREEN + "[G] " + ChatColor.RESET + e.getPlayer().getName() + ": " + content.replaceFirst("!", "");
            message = message
                    .append(Component.text(content.replaceFirst("!", "")));
            for (Player player : Bukkit.getOnlinePlayers()) {
                String playerContent = content;
                Component sendMessage = Component.empty();

                if (playerContent.contains(player.getName())) {
                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, (float) 0.5, (float) 2);
                    String tagName = ChatColor.AQUA + player.getName() + ChatColor.RESET;
                    playerContent = playerContent.replace(player.getName(), tagName);
                }

                if (player.hasPermission("betterinteraction.detelemessage")) {
                    sendMessage = sendMessage
                            .append(deleteButton)
                            .append(global)
                            .append(nickname)
                            .append(messageColon)
                            .append(Component.text(playerContent.replaceFirst("!", "")));
                } else {
                    sendMessage = sendMessage
                            .append(global)
                            .append(nickname)
                            .append(messageColon)
                            .append(Component.text(playerContent.replaceFirst("!", "")));
                }
                messageQueue.getPlayer(player).addMessage(sendMessage);
                player.sendMessage(sendMessage);


            }
        } else {
            logMessage = ChatColor.YELLOW + "[L] " + ChatColor.RESET + e.getPlayer().getName() + ": " + content;
            message = message
            .append(Component.text(content));
            boolean heard = false;
            for (Player player : Bukkit.getOnlinePlayers()) {
                String playerContent = content;
                if (!location.getWorld().equals(player.getWorld())) continue;
                if (location.distance(player.getLocation()) < 100) {
                    Component sendMessage = Component.empty();

                    if (playerContent.contains(player.getName())) {
                        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, (float) 0.5, (float) 2);
                        String tagName = ChatColor.AQUA + player.getName() + ChatColor.RESET;
                        playerContent = playerContent.replace(player.getName(), tagName);
                    }

                    if (player.hasPermission("betterinteraction.detelemessage")) {
                        sendMessage = sendMessage
                                .append(deleteButton)
                                .append(local)
                                .append(nickname)
                                .append(messageColon)
                                .append(Component.text(playerContent));
                    } else {
                        sendMessage = sendMessage
                                .append(local)
                                .append(nickname)
                                .append(messageColon)
                                .append(Component.text(playerContent));
                    }
                    if (!player.equals(e.getPlayer())) heard = true;
                    messageQueue.getPlayer(player).addMessage(sendMessage);
                    player.sendMessage(sendMessage);
                }
            }
            if (!heard) {
                Component notHeard = Component.text(ChatColor.ITALIC + "Вас никто не услышал. ")
                        .color(TextColor.color(0xFFFF55));
                notHeard = notHeard.append(Component.text(ChatColor.RESET + "[Написать в глобальный чат]")
                        .color(TextColor.color(0x55FF55))
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Или просто напиши "+ ChatColor.GREEN + "!" + ChatColor.RESET + " в начале сообщения")))
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "!" + content))
                );
                e.getPlayer().sendMessage(notHeard);
                messageQueue.getPlayer(e.getPlayer()).addMessage(notHeard);
            }
        }
        messages.add(message);
        logger.log(Level.INFO, logMessage);
    }

}
