package hikko.betterinteraction.customChat;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;

public class EmotesFilter {

    ArrayList<String> emotes = new ArrayList<>();
    ArrayList<String> unicodeEmotes = new ArrayList<>();

    EmotesFilter() {
        addEmote("Kappa", "\\ue031");
        addEmote("Jebaited", "\\ue030");
        addEmote("Kekw", "\\ue032");
    }

    private void addEmote(String emote, String unicode) {
        emotes.add(emote);
        unicodeEmotes.add(unicode);
    }

    public ArrayList<String> getEmotes() {
        return emotes;
    }

    public ArrayList<String> getUnicodeEmotes() {
        return unicodeEmotes;
    }

    public String getEmotes(String m) {
        String message = m;

        for (String emoji : emotes) {
            if (message.contains(emoji)) {
                message = message.replace(emoji, getUnicodeEmoji(emoji));
            }
            if (message.contains(emoji.toLowerCase())) {
                message = message.replace(emoji.toLowerCase(), getUnicodeEmoji(emoji));
            }
            if (message.contains(emoji.toUpperCase())) {
                message = message.replace(emoji.toUpperCase(), getUnicodeEmoji(emoji));
            }
        }

        return message;
    }

    private String getUnicodeEmoji(String message) {
        return StringEscapeUtils.unescapeJava(unicodeEmotes.get(emotes.indexOf(message)));
    }
}
