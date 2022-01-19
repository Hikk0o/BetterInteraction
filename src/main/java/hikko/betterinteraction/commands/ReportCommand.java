package hikko.betterinteraction.commands;

import com.google.common.collect.Lists;
import hikko.betterinteraction.BetterInteraction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ReportCommand extends AbstractCommand {

    public ReportCommand() {
        super("report");
        BetterInteraction.getInstance().getLogger().log(Level.INFO, "Loading report commands...");
    }


    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(ChatColor.YELLOW + "Используйте /report [ник] [сообщение]");
        } else {
            try {
                String webhookURL = "https://discord.com/api/webhooks/933332923866808320/4F_K8eLXjm_GBn4tDpnYpciYWZMiaPZThm-xT7TnJixRms6zL9_yZ2YG1sCKfeE6G-SS";
                final HttpsURLConnection connection = (HttpsURLConnection) new URL(webhookURL).openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11");
                connection.setDoOutput(true);
                try (final OutputStream outputStream = connection.getOutputStream()) {
                    // Handle backslashes.
                    StringBuilder reportMessage = new StringBuilder();
                    for (int a = 1; a < args.length; a++) reportMessage.append(args[a]).append(" ");
                    outputStream.write(("{\n" +
                            "  \"content\": \"<@&933380559768547408>\",\n" +
                            "  \"embeds\": [\n" +
                            "    {\n" +
                            "      \"title\": \"New report!\",\n" +
                            "      \"color\": 36863,\n" +
                            "      \"fields\": [\n" +
                            "        {\n" +
                            "          \"name\": \"Report from\",\n" +
                            "          \"value\": \""+sender.getName()+"\",\n" +
                            "          \"inline\": true\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"name\": \"Report on\",\n" +
                            "          \"value\": \""+args[0]+"\",\n" +
                            "          \"inline\": true\n" +
                            "        },\n" +
                            "        {\n" +
                            "          \"name\": \"Message\",\n" +
                            "          \"value\": \""+reportMessage+"\"\n" +
                            "        }\n" +
                            "      ]\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}").getBytes(StandardCharsets.UTF_8));
                }
                connection.getInputStream();
            } catch (final IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        ArrayList<String> list = Lists.newArrayList();
        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                list.add(player.getName());
            }
            return list;
        }
        return Lists.newArrayList();
    }
}
