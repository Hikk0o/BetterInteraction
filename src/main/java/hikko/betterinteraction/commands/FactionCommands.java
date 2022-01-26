package hikko.betterinteraction.commands;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import hikko.betterinteraction.BetterInteraction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class FactionCommands extends AbstractCommand {

    public FactionCommands() {
        super("chat");
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Loading Faction commands...");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Используйте /chat [сообщение для вашей фракции]");
            return;
        }

        FPlayer fsender = FPlayers.getInstance().getByPlayer(BetterInteraction.getInstance().getServer().getPlayer(sender.getName()));

        if (fsender.getFaction().isWilderness()) {
            sender.sendMessage(ChatColor.YELLOW + "Вы должны быть во фракции, чтобы использовать эту команду.");
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            FPlayer fplayer = FPlayers.getInstance().getByPlayer(BetterInteraction.getInstance().getServer().getPlayer(player.getName()));

            if (fsender.getFaction().equals(fplayer.getFaction())) {
                Component message = Component.empty();
                message = message
                        .append(Component.text("[").color(TextColor.color(0xFFFFFF)))
                        .append(Component.text(fsender.getFaction().getTag()).color(TextColor.color(0x55FF55))
                                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Чат вашей фракции").color(TextColor.color(0x55FF55)))))
                        .append(Component.text("] ").color(TextColor.color(0xFFFFFF)))
                        .append(Component.text(fsender.getName()).color(TextColor.color(0x55FF55))
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/w " + fsender.getPlayer().getName() + " "))
                                .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text(ChatColor.GREEN + "Отправить личное сообщение игроку " + ChatColor.WHITE + fsender.getPlayer().getName()))))
                        .append(Component.text(": ").color(TextColor.color(0xFFFFFF)));

                StringBuilder msg = new StringBuilder();
                for (String str : args) {
                    if (str.equals(player.getName())) {
                        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_PLACE, (float) 0.5, (float) 2);
                        str = ChatColor.AQUA + player.getName() + ChatColor.RESET;
                    }
                    msg.append(str).append(" ");
                }
                String strMessage = BetterInteraction.getInstance().getChatEvents().getEmotesFilter().getEmotes(msg.toString());
                message = message.append(Component.text(strMessage).color(TextColor.color(0xFFFFFF)));

                player.sendMessage(message);
                BetterInteraction.getInstance().getChatEvents().getMessageQueue().getPlayer(player).addMessage(message);
            }
        }


    }
}
