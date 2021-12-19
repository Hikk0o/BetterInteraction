package hikko.betterperformance.itemLogger;

import hikko.betterperformance.BetterPerformance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class itemEvents implements Listener {

    public itemEvents() {
        BetterPerformance.getInstance().getLogger().log(Level.INFO, "Loading events items...");
    }

    final Logger logger = Logger.getLogger("Item Events");
    final SimpleDateFormat format = new SimpleDateFormat("d.M.y");
    final SimpleDateFormat time = new SimpleDateFormat("[HH:mm:ss] ");
    PrintWriter writer;

    void openFile() {
        File file = new File(BetterPerformance.getInstance().getDataFolder() + "/logs/" + format.format(Calendar.getInstance().getTime()) + ".log");
        try {
            writer = new PrintWriter(new FileWriter(file,true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    final BukkitScheduler scheduler = BetterPerformance.getInstance().getServer().getScheduler();
    @EventHandler
    public void ItemDespawn(ItemDespawnEvent e) {
        scheduler.runTaskAsynchronously(BetterPerformance.getInstance(), () -> {
            openFile();
            writer.println(time.format(Calendar.getInstance().getTime()) + "ItemDespawnEvent: {item: " + e.getEntity().getName() + " x " + e.getEntity().getItemStack().getAmount() + "}");
            if (BetterPerformance.getInstance().getConfig().getBoolean("logsToConsole")) {
                logger.log(Level.INFO, "ItemDespawnEvent: {item: " + e.getEntity().getName() + " x " + e.getEntity().getItemStack().getAmount() + "}");
            }
            writer.close();
        });
    }

    @EventHandler
    public void PlayerDropItem(PlayerDropItemEvent e) {
        scheduler.runTaskAsynchronously(BetterPerformance.getInstance(), () -> {
            openFile();
            writer.println(time.format(Calendar.getInstance().getTime()) + "PlayerDropItemEvent: {item: " + e.getItemDrop().getName() + " x " + e.getItemDrop().getItemStack().getAmount() + "; owner: "+ e.getPlayer().getName() +"}");
            if (BetterPerformance.getInstance().getConfig().getBoolean("logsToConsole")) {
                logger.log(Level.INFO, "PlayerDropItemEvent: {item: " + e.getItemDrop().getName() + " x " + e.getItemDrop().getItemStack().getAmount() + "; owner: "+ e.getPlayer().getName() +"}");
            }
            writer.close();
        });
    }

    @EventHandler
    public void PlayerDeath(PlayerDeathEvent e) {
        scheduler.runTaskAsynchronously(BetterPerformance.getInstance(), () -> {
            openFile();
            writer.println(time.format(Calendar.getInstance().getTime()) + "PlayerDeathEvent: {items: " + e.getDrops() + "; owner: "+ e.getPlayer().getName() +"}");
            if (BetterPerformance.getInstance().getConfig().getBoolean("logsToConsole")) {
                logger.log(Level.INFO, "PlayerDeathEvent: {items:" + e.getDrops() + "; owner: "+ e.getPlayer().getName() +"}");
            }
            writer.close();
        });
    }
}
