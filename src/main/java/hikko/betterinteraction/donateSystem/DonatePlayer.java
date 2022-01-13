package hikko.betterinteraction.donateSystem;

import hikko.betterinteraction.BetterInteraction;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class DonatePlayer {

    private final String name;
    private boolean coloredNickname = false;
    private boolean isSponsor = false;
    private boolean isHaveEffects = false;

    public DonatePlayer(String name) {
        this.name = name;
        ArrayList<String> purchase = BetterInteraction.getInstance().getDonateDatabase().getPlayerPurchase(name);
        if (purchase != null && !purchase.isEmpty()) {
            for (String value : purchase) {
                if (value.equals("coloredNickname")) {
                    coloredNickname = true;
                }
                if (value.equals("sponsor")) {
                    isSponsor = true;
                    setHaveEffects(true);
                }
                if (value.equals("particleMenu")) {
                    isHaveEffects = true;
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
    public boolean isHaveEffects() {
        return isHaveEffects;
    }

    public void setColoredNickname(boolean coloredNickname) {
        this.coloredNickname = coloredNickname;
    }
    public void setSponsor(boolean sponsor) {
        isSponsor = sponsor;
        setHaveEffects(true);
    }
    public void setHaveEffects(boolean haveEffects) {
        if (haveEffects) {
            Player player = BetterInteraction.getInstance().getServer().getPlayer(this.name);
            if (player == null) return;
            LuckPerms api = LuckPermsProvider.get();
            User user = api.getPlayerAdapter(Player.class).getUser(player);
            DataMutateResult result = user.data().add(Node.builder("group.donateplayer").build());
            api.getUserManager().saveUser(user);
        }
        isHaveEffects = haveEffects;
    }
}
