package hikko.betterinteraction.clearEntities;

import com.destroystokyo.paper.event.entity.PlayerNaturallySpawnCreaturesEvent;
import hikko.betterinteraction.BetterInteraction;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CheckerEvents implements Listener {

    @EventHandler
    public void SpawnMonstersEvent(PlayerNaturallySpawnCreaturesEvent e) {
        boolean checkerTPS = BetterInteraction.getInstance().getConfig().getBoolean("prices.coloredNickname.cost");

        if (checkerTPS) {
            Server server = BetterInteraction.getInstance().getServer();
            World world = server.getWorld("world");

            if (world != null && server.getTPS()[0] <= 19) {
                if (world.getTicksPerMonsterSpawns() >= 2 && world.getTicksPerMonsterSpawns() <= 40) {
                    world.setTicksPerMonsterSpawns((int) (world.getTicksPerMonsterSpawns()+2));
                }
            } else {
                if (world != null && world.getTicksPerMonsterSpawns() >= 4 && world.getTicksPerMonsterSpawns() <= 40) {
                    world.setTicksPerMonsterSpawns((int) (world.getTicksPerMonsterSpawns()-2));
                }
            }
        }
    }

}
