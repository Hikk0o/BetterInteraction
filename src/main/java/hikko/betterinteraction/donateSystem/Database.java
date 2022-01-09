package hikko.betterinteraction.donateSystem;

import hikko.betterinteraction.BetterInteraction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
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
        if (product.equals("coloredNickname")) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(currentTime);
            cal.add(Calendar.MONTH, 1);
            String query =
                    "INSERT INTO playersProducts (name, product, endDate) " +
                    "VALUES ('"+nickname+"','"+product+"','"+cal.getTimeInMillis()+"')";
            int nowMonth = cal.get(Calendar.MONTH) + 1;
            try {
                PreparedStatement statement = conn.prepareStatement(query);
                statement.executeUpdate();
                BetterInteraction.getInstance().getDonateDatabase().getPlayer(nickname).setColoredNickname(true);
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
                    long currentTime = System.currentTimeMillis();
                    Calendar nowDate = Calendar.getInstance();
                    nowDate.setTimeInMillis(currentTime);
                    Player player = BetterInteraction.getInstance().getServer().getPlayer(nickname);
                    String dataProduct = response.getString("product");

                    Calendar endDate = Calendar.getInstance();
                    endDate.setTimeInMillis(response.getLong("endDate"));
                    if (nowDate.getTimeInMillis() > endDate.getTimeInMillis()) {
                        if (player != null) {
                            if (dataProduct.equals("coloredNickname")) {
                                player.sendMessage("[Donate] Срок подписки \"Цветной ник\" закончился.");
                            }
                        }
                        String query2 =
                                "DELETE FROM playersProducts " +
                                        "WHERE id = '"+response.getInt("id")+"'";
                        Statement statement2 = conn.createStatement();
                        statement2.executeUpdate(query2);

                    } else {
                        purchase.add(response.getString("product"));
                        if (player != null) {
                            String product = "";
                            if (dataProduct.equals("coloredNickname")) product = "\"Цветной ник\"";
                            if (dataProduct.equals("sponsor")) product = "\"Спонсор\"";

                            long days = response.getLong("endDate");

                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(days);
                            int endMonth = cal.get(Calendar.MONTH) + 1;

                            String date = "";
                            if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
                                date += "0"+cal.get(Calendar.DAY_OF_MONTH)+".";
                            } else {
                                date += cal.get(Calendar.DAY_OF_MONTH)+".";
                            }
                            if (endMonth < 10) {
                                date += "0"+endMonth+".";
                            } else {
                                date += endMonth+".";
                            }
                            date += cal.get(Calendar.YEAR);

                            String time = "";

                            if (cal.get(Calendar.HOUR_OF_DAY) < 10) {
                                time += "0"+cal.get(Calendar.HOUR_OF_DAY)+":";
                            } else {
                                time += cal.get(Calendar.HOUR_OF_DAY)+":";
                            }
                            if (cal.get(Calendar.MINUTE) < 10) {
                                time += "0"+cal.get(Calendar.MINUTE);
                            } else {
                                time += cal.get(Calendar.MINUTE);
                            }


                            player.sendMessage("[Donate] Срок подписки "+product+" заканчивается " + date + " в " + time + " по МСК");

                            int nowMonth = nowDate.get(Calendar.MONTH) + 1;
                        }
                    }
                }
            }
            return purchase;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
