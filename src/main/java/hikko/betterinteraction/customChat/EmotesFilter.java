package hikko.betterinteraction.customChat;

import hikko.betterinteraction.BetterInteraction;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;
import java.util.logging.Level;

public class EmotesFilter {

    ArrayList<String> emotes = new ArrayList<>();
    ArrayList<String> unicodeEmotes = new ArrayList<>();

    EmotesFilter() {
        emotes.add("Jebaited");
        unicodeEmotes.add("\\ue030");
        emotes.add("Kappa");
        unicodeEmotes.add("\\ue031");
        emotes.add("Kekw");
        unicodeEmotes.add("\\ue032");
    }



    public String getEmotes(String m) {
        String message = m;

        for (String emoji : emotes) {
            BetterInteraction.getInstance().getLogger().log(Level.INFO, "Find \"" + emoji + "\" in \"" + message + "\"");
            if (message.contains(emoji)) {
//                BetterInteraction.getInstance().getLogger().log(Level.INFO, "Replace " + emoji + " on " + getUnicodeEmoji(emoji));
                message = message.replace(emoji, getUnicodeEmoji(emoji));
            }
            if (message.contains(emoji.toLowerCase())) {
//                BetterInteraction.getInstance().getLogger().log(Level.INFO, "Replace " + emoji + " on " + getUnicodeEmoji(emoji));
                message = message.replace(emoji.toLowerCase(), getUnicodeEmoji(emoji));
            }
            if (message.contains(emoji.toUpperCase())) {
//                BetterInteraction.getInstance().getLogger().log(Level.INFO, "Replace " + emoji + " on " + getUnicodeEmoji(emoji));
                message = message.replace(emoji.toUpperCase(), getUnicodeEmoji(emoji));
            }
        }

        return message;
    }

    private String getUnicodeEmoji(String message) {

        BetterInteraction.getInstance().getLogger().log(Level.INFO, unicodeEmotes.get(emotes.indexOf(message)));
        return StringEscapeUtils.unescapeJava(unicodeEmotes.get(emotes.indexOf(message)));
    }
}
