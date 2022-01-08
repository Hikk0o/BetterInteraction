package hikko.betterinteraction.donateSystem;

import hikko.betterinteraction.BetterInteraction;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.logging.Level;

public class DonatePlayer {

    String name;
    boolean coloredNickname = false;
    boolean isSponsor = false;

    public DonatePlayer(String name) {
        this.name = name;
        ArrayList<String> purchase = BetterInteraction.getInstance().getDonateDatabase().getPlayerPurchase(name);
        if (purchase != null && !purchase.isEmpty()) {
            for (String value : purchase) {
                if (value.equals("coloredNickname")) {
                    coloredNickname = true;
                }
                if (value.equals("isSponsor")) {
                    coloredNickname = true;
                    isSponsor = true;
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public boolean isColoredNickname() {
        return coloredNickname;
    }
}
