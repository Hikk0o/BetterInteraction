package hikko.loggeritems;

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

public class events implements Listener {

    Logger logger = Logger.getLogger("Item Events");
    SimpleDateFormat format = new SimpleDateFormat("d.M.y");
    SimpleDateFormat time = new SimpleDateFormat("[HH:mm:ss] ");
    PrintWriter writer;

    void openFile() {
        File file = new File(LoggerItems.getInstance().getDataFolder() + "/" + format.format(Calendar.getInstance().getTime()) + ".log");
        try {
            writer = new PrintWriter(new FileWriter(file,true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void ItemDespawn(ItemDespawnEvent e) {
        BukkitScheduler scheduler = LoggerItems.getInstance().getServer().getScheduler();
        scheduler.runTaskAsynchronously(LoggerItems.getInstance(), () -> {
            openFile();
            writer.println(time.format(Calendar.getInstance().getTime()) + "ItemDespawnEvent: {item: " + e.getEntity().getName() + " x " + e.getEntity().getItemStack().getAmount() + "; owner: "+ e.getEntity().getOwner() +"}");
            if (Boolean.parseBoolean(LoggerItems.getInstance().getConfig().getString("logsToConsole"))) {
                logger.log(Level.INFO, "ItemDespawnEvent: {item: " + e.getEntity().getName() + " x " + e.getEntity().getItemStack().getAmount() + "; owner: "+ e.getEntity().getOwner() +"}");
            }
            writer.close();
        });
    }

    @EventHandler
    public void PlayerDropItem(PlayerDropItemEvent e) {
        BukkitScheduler scheduler = LoggerItems.getInstance().getServer().getScheduler();
        scheduler.runTaskAsynchronously(LoggerItems.getInstance(), () -> {
            openFile();
            writer.println(time.format(Calendar.getInstance().getTime()) + "PlayerDropItemEvent: {item: " + e.getItemDrop().getName() + " x " + e.getItemDrop().getItemStack().getAmount() + "; owner: "+ e.getPlayer().getName() +"}");
            if (Boolean.parseBoolean(LoggerItems.getInstance().getConfig().getString("logsToConsole"))) {
                logger.log(Level.INFO, "PlayerDropItemEvent: {item: " + e.getItemDrop().getName() + " x " + e.getItemDrop().getItemStack().getAmount() + "; owner: "+ e.getPlayer().getName() +"}");
            }
            writer.close();
        });
    }

    @EventHandler
    public void PlayerDeath(PlayerDeathEvent e) {
        BukkitScheduler scheduler = LoggerItems.getInstance().getServer().getScheduler();
        scheduler.runTaskAsynchronously(LoggerItems.getInstance(), () -> {
            openFile();
            writer.println(time.format(Calendar.getInstance().getTime()) + "PlayerDeathEvent: {items: " + e.getDrops() + "; owner: "+ e.getPlayer().getName() +"}");
            if (Boolean.parseBoolean(LoggerItems.getInstance().getConfig().getString("logsToConsole"))) {
                logger.log(Level.INFO, "PlayerDeathEvent: {items:" + e.getDrops() + "; owner: "+ e.getPlayer().getName() +"}");
            }
            writer.close();
        });
    }
}
