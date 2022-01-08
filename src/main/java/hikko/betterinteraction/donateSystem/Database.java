package hikko.betterinteraction.donateSystem;

import hikko.betterinteraction.BetterInteraction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class Database {

    private Connection conn;
    private final List<DonatePlayer> donatePlayers = new ArrayList<>();


    public Database() {

    }

    public void open() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + BetterInteraction.getInstance().getDataFolder() + "/users.db");
            BetterInteraction.getInstance().getLogger().log(Level.INFO, ChatColor.GREEN + "Connected to DB!");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            conn.close();
            BetterInteraction.getInstance().getLogger().log(Level.INFO, ChatColor.YELLOW + "Disconnected from DB!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DonatePlayer> getDonatePlayers() {
        return donatePlayers;
    }

    public void addPlayer(Player player) {
        String name = player.getName();
        donatePlayers.add(new DonatePlayer(name));
        String query =
                "INSERT OR IGNORE INTO players (name, donate) " +
                "VALUES ('"+name+"','0')";
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public DonatePlayer getPlayer(String nickname) {

        for (DonatePlayer donatePlayer : donatePlayers) {
            if (donatePlayer.getName().equals(nickname)) {
//                BetterPerformance.getInstance().getLogger().log(Level.WARNING, queuePlayerMessages.getPlayer());
                return donatePlayer;
            }
        }
        return null;
    }

    public Object getDonate(String nickname) {
        String query =
                "SELECT id, name, donate " +
                "FROM players " +
                "WHERE name = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, nickname);
            ResultSet response = statement.executeQuery();
            if (!response.isClosed()) {
                return response.getInt("donate");
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean setDonate(String nickname, int donate) {
        String query =
                "UPDATE players " +
                "SET donate = ? " +
                "WHERE name = ?";
        try {
            if (getDonate(nickname) != null) {
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setInt(1, donate);
                statement.setString(2, nickname);
                statement.executeUpdate();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean addPlayerPurchase(String nickname, String product) {
        long currentTime = System.currentTimeMillis();
        if (product.equals("changeColorNickname")) {
            int days = 30;
            long month = 1000L*60*60*24*days;
            long endDate = currentTime + month;
            Date date = new Date(endDate);
            System.out.println(date);
            String query =
                    "INSERT INTO playersProducts (name, product, endDate) " +
                            "VALUES ('"+nickname+"','"+product+"','"+endDate+"')";
            try {
                PreparedStatement statement = conn.prepareStatement(query);
                statement.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public ArrayList<String> getPlayerPurchase(String nickname) {
        String query =
                "SELECT id, name, product, endDate " +
                        "FROM playersProducts " +
                        "WHERE name = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, nickname);
            ResultSet response = statement.executeQuery();
            ArrayList<String> purchase = new ArrayList<>();
            if (!response.isClosed()) {
                while (response.next()) {
                    purchase.add(response.getString("product"));
                }
            }
            return purchase;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
