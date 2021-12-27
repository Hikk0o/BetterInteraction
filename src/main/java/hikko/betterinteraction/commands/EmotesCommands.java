package hikko.betterinteraction.commands;

import hikko.betterinteraction.BetterInteraction;
import hikko.betterinteraction.customChat.EmotesFilter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class EmotesCommands extends AbstractCommand {

    public EmotesCommands() {
        super("emotes");
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Loading emotes commands...");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            Component message = Component.empty();
            message = message
                    .append(Component.text(ChatColor.GREEN + "Список эмоций " + ChatColor.YELLOW + "(подписаны при наведении мышкой)" + ChatColor.GREEN + ": "))
                    .append(Component.newline());

            EmotesFilter emotesFilter = BetterInteraction.getInstance().getChatEvents().getEmotesFilter();

            for (int a = 0; a < emotesFilter.getEmotes().size(); a++) {
                message = message
                        .append(Component.text(StringEscapeUtils.unescapeJava(emotesFilter.getUnicodeEmotes().get(a)))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(emotesFilter.getEmotes().get(a)))))
                        .append(Component.space());
            }
            sender.sendMessage(message);
            Player player = BetterInteraction.getInstance().getServer().getPlayer(sender.getName());
            BetterInteraction.getInstance().getChatEvents().getMessageQueue().getPlayer(player).addMessage(message);
        }
    }
}
