package hikko.betterinteraction;

import hikko.betterinteraction.donateSystem.DonatePlayer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
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

    final SimpleDateFormat format = new SimpleDateFormat("d.M.y");
    final SimpleDateFormat time = new SimpleDateFormat("[HH:mm:ss] ");
    PrintWriter writer;

    private void openFile() {
        File file = new File(BetterInteraction.getInstance().getDataFolder() + "/transactionsLogs/" + format.format(Calendar.getInstance().getTime()) + ".log");
        try {
            this.writer = new PrintWriter(new FileWriter(file,true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPlayer(Player player) {
        String name = player.getName();
        donatePlayers.add(new DonatePlayer(name));
        if (getDonate(player.getName()) == null) {
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
    }

    public DonatePlayer getPlayer(String nickname) {

        for (DonatePlayer donatePlayer : donatePlayers) {
            if (donatePlayer.getName().equals(nickname)) {
                return donatePlayer;
            }
        }
        return null;
    }

    public Object getDonate(String nickname) {
        String query =
                "SELECT name, donate " +
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
                openFile();
                this.writer.println(time.format(Calendar.getInstance().getTime()) + "setDonate: { player: " + nickname + "; setDonate: " + donate + " }");
                this.writer.close();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getTimeLeft(Calendar cal, String product) {

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


        return ChatColor.YELLOW + "Срок подписки " + ChatColor.WHITE + product + ChatColor.YELLOW + " заканчивается " + ChatColor.WHITE + date + ChatColor.YELLOW + " в " + ChatColor.WHITE + time + ChatColor.YELLOW + " по МСК";
    }

    public boolean addPlayerPurchase(String nickname, String product) {
        long currentTime = System.currentTimeMillis();

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
                if (product.equals("coloredNickname")) getPlayer(nickname).setColoredNickname(true);
                if (product.equals("particleMenu")) getPlayer(nickname).setHaveEffects(true);
                if (product.equals("sponsor")) getPlayer(nickname).setSponsor(true);
                openFile();
                this.writer.println(time.format(Calendar.getInstance().getTime()) + "addPlayerProduct: { player: " + nickname + "; product: " + product + "; endData: "+cal+" }");
                this.writer.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

    }

    private void removePermissionGroup(Player player, String groupName) {
        groupName = "group." + groupName;
        if (player.hasPermission(groupName)) {
            LuckPerms api = LuckPermsProvider.get();
            User user = api.getPlayerAdapter(Player.class).getUser(player);
            user.data().remove(Node.builder(groupName).build());
            api.getUserManager().saveUser(user);
        }
        if (groupName.equals("group.donateplayer")) {
            CommandSender console = BetterInteraction.getInstance().getServer().getConsoleSender();
            BetterInteraction.getInstance().getServer().dispatchCommand(console, "h clear " + player.getName());
        }
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
                            String product = "";
                            if (dataProduct.equals("coloredNickname")) product = "\"Цветной ник\"";
                            if (dataProduct.equals("sponsor")) {
                                product = "\"Спонсор\"";
                                removePermissionGroup(player, "donateplayer");
                            }
                            if (dataProduct.equals("particleMenu")) {
                                product = "\"Меню эффектов\"";
                                removePermissionGroup(player, "donateplayer");
                            }

                            player.sendMessage(ChatColor.YELLOW + "Срок подписки "+ChatColor.WHITE+product+ChatColor.YELLOW+" закончился.");

                            String query2 =
                                    "DELETE FROM playersProducts " +
                                            "WHERE id = '"+response.getInt("id")+"'";
                            Statement statement2 = conn.createStatement();
                            statement2.executeUpdate(query2);

                        } else {
                            BetterInteraction.getInstance().getLogger().log(Level.SEVERE, "Player null!");
                        }

                    } else {
                        purchase.add(response.getString("product"));
                        if (player != null) {

                            String product = "";
                            if (dataProduct.equals("coloredNickname")) product = "\"Цветной ник\"";
                            if (dataProduct.equals("sponsor")) product = "\"Спонсор\"";
                            if (dataProduct.equals("particleMenu")) product = "\"Меню эффектов\"";

                            long days = response.getLong("endDate");

                            Calendar cal = Calendar.getInstance();
                            cal.setTimeInMillis(days);

                            player.sendMessage(getTimeLeft(cal, product));
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
