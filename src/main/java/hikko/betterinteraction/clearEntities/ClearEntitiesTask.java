package hikko.betterinteraction.clearEntities;

import hikko.betterinteraction.BetterInteraction;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

class ClearEntitiesTask extends BukkitRunnable {

    int counter = 0;

    @Override
    public void run()
    {
        if (!CheckerEntities.pigs.isEmpty()) {
            Entity entity = CheckerEntities.pigs.get(0);
            entity.remove();
            counter++;
            CheckerEntities.pigs.remove(0);
/*
            LoggerItems.getInstance().getLogger().log(Level.INFO, ChatColor.RED + "Kill PIG");
*/
        }
        if (!CheckerEntities.chikens.isEmpty()) {
            Entity entity = CheckerEntities.chikens.get(0);
            entity.remove();
            counter++;
            CheckerEntities.chikens.remove(0);
/*
            LoggerItems.getInstance().getLogger().log(Level.INFO, ChatColor.RED + "Kill CHICKEN");
*/
        }
        if (!CheckerEntities.cows.isEmpty()) {
            Entity entity = CheckerEntities.cows.get(0);
            entity.remove();
            counter++;
            CheckerEntities.cows.remove(0);
/*
            LoggerItems.getInstance().getLogger().log(Level.INFO, ChatColor.RED + "Kill COW");
*/
        }
        if (!CheckerEntities.sheeps.isEmpty()) {
            Entity entity = CheckerEntities.sheeps.get(0);
            entity.remove();
            counter++;
            CheckerEntities.sheeps.remove(0);
/*
            LoggerItems.getInstance().getLogger().log(Level.INFO, ChatColor.RED + "Kill SHEEP");
*/
        }

        if (CheckerEntities.pigs.isEmpty() && CheckerEntities.chikens.isEmpty() && CheckerEntities.cows.isEmpty() && CheckerEntities.sheeps.isEmpty()) {
            BetterInteraction.getInstance().getLogger().log(Level.INFO, "Successfully deleted. (Total: " + counter + ")");
            counter = 0;
            CheckerEntities.taskActive = false;
            this.cancel();
        }
    }
}