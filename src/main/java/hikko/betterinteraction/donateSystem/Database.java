package hikko.betterinteraction.donateSystem;

import hikko.betterinteraction.BetterInteraction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.logging.Level;

public class Database {

    Connection conn;

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

    public void addPlayer(Player player) {
        String name = player.getName();
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

    public Object getDonate(String name) {
        String query =
                "SELECT id, name, donate " +
                "FROM players " +
                "WHERE name = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, name);
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
}
