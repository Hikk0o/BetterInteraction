package hikko.betterinteraction.donateSystem;

import hikko.betterinteraction.BetterInteraction;

import java.util.ArrayList;

public class DonatePlayer {

    private final String name;
    private boolean coloredNickname = false;
    private boolean isSponsor = false;

    public DonatePlayer(String name) {
        this.name = name;
        ArrayList<String> purchase = BetterInteraction.getInstance().getDonateDatabase().getPlayerPurchase(name);
        if (purchase != null && !purchase.isEmpty()) {
            for (String value : purchase) {
                if (value.equals("coloredNickname")) {
                    coloredNickname = true;
                }
                if (value.equals("sponsor")) {
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
    public boolean isSponsor() {
        return isSponsor;
    }

    public void setColoredNickname(boolean coloredNickname) {
        this.coloredNickname = coloredNickname;
    }

    public void setSponsor(boolean sponsor) {
        isSponsor = sponsor;
    }
}
