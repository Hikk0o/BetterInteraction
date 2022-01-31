package hikko.betterinteraction.customChat;

import com.earth2me.essentials.User;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import hikko.betterinteraction.BetterInteraction;
import hikko.betterinteraction.Database;
import hikko.betterinteraction.customChat.chat.MessageQueue;
import hikko.betterinteraction.customChat.chat.PlayerMessages;
import hikko.betterinteraction.customChat.protocol.ChatPacketHandler;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.ess3.api.events.PrivateMessagePreSendEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("CommentedOutCode")
public class ChatEvents implements Listener {
    public ChatEvents() {
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Loading chat events...");
        new ChatPacketHandler();
    }

    private final MessageQueue messageQueue = new MessageQueue();
    private final EmotesFilter emotesFilter = new EmotesFilter();
    private final Database database = BetterInteraction.getInstance().getDatabase();
    private final BukkitScheduler scheduler = BetterInteraction.getInstance().getServer().getScheduler();
    private final Logger chatLogger = Logger.getLogger("Chat");
    private List<String> banWords = BetterInteraction.getInstance().getConfig().getStringList("banwords");
    private List<String> fakeBanWords = BetterInteraction.getInstance().getConfig().getStringList("fakebanwords");
    Logger debug = BetterInteraction.getInstance().getLogger();

    public MessageQueue getMessageQueue() {
        return messageQueue;
    }

    public void updateWords() {
        banWords = BetterInteraction.getInstance().getConfig().getStringList("banwords");
        fakeBanWords = BetterInteraction.getInstance().getConfig().getStringList("fakebanwords");
    }

    public boolean hasBanWordInMessage(Player player, String message) {
        message = message.toLowerCase().replaceFirst("!", "");
        for (String word : message.split(" ")) {
            for (String s : banWords) {
                String banword = s.toLowerCase();

                boolean isFakeBanWord = false;
                for (String fakeBanWord : fakeBanWords) {
                    Pattern pattern = Pattern.compile(".*" + fakeBanWord + ".*");
                    Matcher matcher = pattern.matcher(word);
                    if (matcher.find()) {
                        isFakeBanWord = true;
                        break;
                    }
                }
                if (isFakeBanWord) continue;

                Pattern pattern = Pattern.compile(".*" + banword + ".*");
                Matcher matcher = pattern.matcher(word);

                if (matcher.find()) {
                    Component banMessage = Component.empty();
                    banMessage = banMessage
                            .append(Component.text("Ваше сообщение содержит запрещённое ").color(TextColor.color(0xFF5555))
                                    .append(Component.text("слово").decorate(TextDecoration.BOLD)
                                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(banword).decorate(TextDecoration.ITALIC).color(TextColor.color(0x818181))))));
                    messageQueue.getPlayer(player).addMessage(banMessage);
                    player.sendMessage(banMessage);

                    chatLogger.log(Level.INFO, "Word \"" + banword + "\" by "+ player.getName() +" removed.");
                    chatLogger.log(Level.INFO, "Full message: " + message);
                    return true;
                }
            }

        }
        return false;
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

    public void delMessage(int id, Player player) {
        Component message = messages.get(id);
        messageQueue.delMessage(message, player.getName());
    }

    @EventHandler
    public void PrivateMessageSent(PrivateMessagePreSendEvent e) { // Essentials event
        e.setCancelled(true);

        Player player = BetterInteraction.getInstance().getServer().getPlayer(e.getSender().getName());

        PlayerMessages playerMessages = messageQueue.getPlayer(player);
        if (playerMessages != null && player != null) {
            if (messageQueue.getPlayer(player).isCooldown()) {
                player.sendMessage(ChatColor.YELLOW + "Немного подождите, прежде чем отправить сообщение");
                return;
            }
            if (playerMessages.getLastSendMessage().equals(e.getMessage())) {
                player.sendMessage(ChatColor.YELLOW + "Ваше сообщение совпадает с предыдущим");
                return;
            }
            if (hasBanWordInMessage(player, e.getMessage())) return;
            playerMessages.setLastSendMessage(e.getMessage());
        }

        messageQueue.getPlayer(player).setCooldown(true);
        scheduler.runTaskLater(BetterInteraction.getInstance(), () -> {
            if (playerMessages != null) {
                playerMessages.setCooldown(false);
            }
        }, 40);

        String message = emotesFilter.getEmotes(e.getMessage());

        Player recipient = BetterInteraction.getInstance().getServer().getPlayer(e.getRecipient().getName());
        Player sender = BetterInteraction.getInstance().getServer().getPlayer(e.getSender().getName());
        if (recipient != null && sender != null) {
            sender.playSound(sender.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, (float) 0.3, (float) 1.5);
            Component sernderMessage = Component.empty();
            sernderMessage = sernderMessage
                    .append(Component.text("PM для ").color(TextColor.color(0xFF9D1F)))
                    .append(Component.text(recipient.getName()).color(TextColor.color(0xFFDB45)))
                    .append(Component.text(": ").color(TextColor.color(0xFF9D1F)))
                    .append(Component.text(message).color(TextColor.color(0xFFFFFF)));

            sender.sendMessage(sernderMessage);
            messageQueue.getPlayer(sender).addMessage(sernderMessage);

            recipient.playSound(recipient.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, (float) 0.5, (float) 2);
            Component recipientMessage = Component.empty();
            recipientMessage = recipientMessage
                    .append(Component.text("PM от ").color(TextColor.color(0xFF9D1F)))
                    .append(Component.text(sender.getName())
                            .color(TextColor.color(0xFFDB45))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Ответить игроку ").color(TextColor.color(0xFF9D1F)).append(Component.text(sender.getName()).color(TextColor.color(0xFFDB45)))))
                            .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/w " + sender.getName() + " ")))
                    .append(Component.text(": ").color(TextColor.color(0xFF9D1F)))
                    .append(Component.text(message).color(TextColor.color(0xFFFFFF)));

            recipient.sendMessage(recipientMessage);
            messageQueue.getPlayer(recipient).addMessage(recipientMessage);

            Logger.getLogger("PM").log(Level.INFO, "От " + sender.getName() + " для " + recipient.getName() + ": " + e.getMessage());

            User senderr = BetterInteraction.getInstance().getAPIEssentials().getUser(sender);
            User recipientt = BetterInteraction.getInstance().getAPIEssentials().getUser(recipient);
            senderr.setReplyRecipient(e.getRecipient());
            recipientt.setReplyRecipient(e.getSender());
        }


    }

    @EventHandler
    public void DeathEvent(PlayerDeathEvent e) {
        if (!e.isCancelled()) {
            Player player = e.getPlayer();
            Component message = Component.empty();
            String world;
            if (player.getLocation().getWorld().getName().equals("world")) {
                world = "Верхний мир";
            } else if (player.getLocation().getWorld().getName().equals("world_the_end")) {
                world = "Энд";
            } else if (player.getLocation().getWorld().getName().equals("world_nether")) {
                world = "Ад";
            } else {
                world = "Неизвестный мир";
            }
            message = message
                    .append(Component.text("Координаты вашей смерти:").color(TextColor.color(0xFF9D1F)))
                    .append(Component.newline())
                    .append(Component.text("X: ").color(TextColor.color(0xFFFF55)))
                    .append(Component.text(player.getLocation().getBlockX()).color(TextColor.color(0xFFFFFF)))
                    .append(Component.space())
                    .append(Component.text("Y: ").color(TextColor.color(0xFFFF55)))
                    .append(Component.text(player.getLocation().getBlockY()).color(TextColor.color(0xFFFFFF)))
                    .append(Component.space())
                    .append(Component.text("Z: ").color(TextColor.color(0xFFFF55)))
                    .append(Component.text(player.getLocation().getBlockZ()).color(TextColor.color(0xFFFFFF)))
                    .append(Component.newline())
                    .append(Component.text("Мир: ").color(TextColor.color(0xFFFF55)))
                    .append(Component.text(world).color(TextColor.color(0xFFFFFF)));
            player.sendMessage(message);
            messageQueue.getPlayer(player).addMessage(message);
        }
    }

    public static final ArrayList<Component> messages = new ArrayList<>();

    int conuter = 0;

    @EventHandler(priority = EventPriority.LOW)
    public void ChatEvent(AsyncChatEvent e) {
        if (e.isCancelled()) return;
        e.setCancelled(true);

        int id = conuter;

        Player sender = e.getPlayer();

        if (messageQueue.getPlayer(sender).isCooldown()) {
            sender.sendMessage(ChatColor.YELLOW + "Немного подождите, прежде чем отправить сообщение");
            return;
        }

        Component message = Component.empty();
        Component nickname = Component.empty();

        FPlayer fsender = FPlayers.getInstance().getByPlayer(sender);

//        if (e.getPlayer().hasPermission("betterinteraction.moderator")) {
//
//            String permissions = "";
//            if (e.getPlayer().hasPermission("betterinteraction.detelemessage")) permissions += "- Удалять сообщения";
//            nickname = nickname.append(
//                    Component.text("\uD83D\uDEE1 ").color(TextColor.color(0xAA00)) // Значок модератора
//                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(ChatColor.GREEN + "Модератор\n" +
//                                    ChatColor.GRAY + "Этот игрок может:\n" + permissions))));
//
//        }

        if (database.getPlayer(sender.getName()).isSponsor()) {
            nickname = nickname.append(
                    Component.text("✦ ").color(TextColor.color(0xC580FF)) // Значок спонсора
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Спонсор\n").color(TextColor.color(0xB38AFF))
                                    .append(Component.text("Этот игрок является спонсором сервера").color(TextColor.color(0xD6C3E8))))));

        }
        Component nick = Component.text(sender.getName());

        if (database.getPlayer(sender.getName()).isColoredNickname()) {
            nick = nick.color(TextColor.color(0x9CA2F0));
        }
        if (database.getPlayer(sender.getName()).isSponsor()) {
            nick = nick.color(TextColor.color(0xAC8BFF));
        }


        Component descNickname = Component.empty();
//        if (fsender.getFaction().isWilderness()) {
//          descNickname = Component.text(fsender.getName() + ChatColor.YELLOW + " не состоит во фракции");
//        } else {
//          descNickname = Component.text(fsender.getName() + ChatColor.YELLOW + " является участником фракции " + ChatColor.RESET + fsender.getFaction().getTag());
//        }
//        descNickname = descNickname
//                .append(Component.newline())
//                .append(Component.newline());

        nickname = nickname
                .append(nick)
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/w " + e.getPlayer().getName() + " "))
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, descNickname.append(Component.text(ChatColor.GREEN+"Нажмите "+ChatColor.WHITE+"ПКМ"+ChatColor.GREEN+", чтобы отправить личное сообщение игроку " + ChatColor.WHITE + e.getPlayer().getName()))));



        Component messageColon = Component.text(": ")
                .color(TextColor.color(0x5D5D5D));

        String content = PlainTextComponentSerializer.plainText().serialize(e.message());
        if (hasBanWordInMessage(sender, content)) return;

        Component global = Component.text("[G] ")
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Глобальный чат").color(TextColor.color(0x55FF55))))
                .color(TextColor.color(0x55FF55));

        Component local = Component.text("[L] ")
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Локальный чат").color(TextColor.color(0xFFFF55))))
                .color(TextColor.color(0xAAAAAA));

        Component deleteButton = Component.text("[X] ")
                .color(TextColor.color(0xED4E3F))
                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(ChatColor.RED + "Удалить сообщение\n"+ ChatColor.GRAY + ChatColor.ITALIC + "ID: " + id)))
                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/betterinteraction detelemessage " + id));

        Location location = sender.getLocation();

        message = message
                .append(nickname)
                .append(messageColon);

        content = emotesFilter.getEmotes(content);

        String logMessage;
        if (content.startsWith("!")) {
            logMessage = ChatColor.GREEN + "[G] " + ChatColor.RESET + e.getPlayer().getName() + ": " + content.replaceFirst("!", "");
            message = message
                    .append(Component.text(content.replaceFirst("!", "")));
            PlayerMessages playerMessages = messageQueue.getPlayer(sender);
            if (playerMessages != null) {
                if (playerMessages.getLastSendMessage().equals(content)) {
                    sender.sendMessage(ChatColor.YELLOW + "Ваше сообщение совпадает с предыдущим");
                    return;
                }
                playerMessages.setLastSendMessage(content);
            }
            messageQueue.getPlayer(sender).setCooldown(true);
            scheduler.runTaskLater(BetterInteraction.getInstance(), () -> {
                if (playerMessages != null) {
                    playerMessages.setCooldown(false);
                }
            }, 40);

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
            logMessage = ChatColor.YELLOW + "[L] " + ChatColor.RESET + sender.getName() + ": " + content;
            message = message
            .append(Component.text(content));
            boolean heard = false;

            PlayerMessages playerMessages = messageQueue.getPlayer(sender);
            if (playerMessages != null) {
                if (playerMessages.getLastSendMessage().equals(content)) {
                    sender.sendMessage(ChatColor.YELLOW + "Ваше сообщение совпадает с предыдущим");
                    return;
                }
                playerMessages.setLastSendMessage(content);
            }
            messageQueue.getPlayer(sender).setCooldown(true);
            scheduler.runTaskLater(BetterInteraction.getInstance(), () -> {
                if (playerMessages != null) {
                    playerMessages.setCooldown(false);
                }
            }, 40);

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

                    if (!player.equals(sender) && sender.canSee(player)) heard = true;

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
                sender.sendMessage(notHeard);
                messageQueue.getPlayer(sender).addMessage(notHeard);
                messageQueue.getPlayer(sender).setCooldown(false);
            }
        }
        conuter++;
        messages.add(message);
        chatLogger.log(Level.INFO, logMessage);
    }

    public EmotesFilter getEmotesFilter() {
        return emotesFilter;
    }
}
