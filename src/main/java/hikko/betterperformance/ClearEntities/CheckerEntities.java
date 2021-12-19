package hikko.betterperformance.ClearEntities;

import hikko.betterperformance.BetterPerformance;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;

public class CheckerEntities extends BukkitRunnable {

    public CheckerEntities() {
        BetterPerformance.getInstance().getLogger().log(Level.INFO, "Loading permissions...");
        this.runTaskTimer(BetterPerformance.getInstance(), 240, 200);
    }

    int PIG = 0;
    int CHICKEN = 0;
    int COW = 0;
    int SHEEP = 0;
    static int MAXCOUNT;
    static boolean taskActive = false;
    public static ArrayList<Entity> pigs = new ArrayList<>();
    public static ArrayList<Entity> chikens = new ArrayList<>();
    public static ArrayList<Entity> cows = new ArrayList<>();
    public static ArrayList<Entity> sheeps = new ArrayList<>();

    void checkEntity(Entity entity) {
        if (entity.getType().name().equals("PIG")) {
            PIG++;
            if (PIG > MAXCOUNT) {
                pigs.add(entity);
            }
        }
        if (entity.getType().name().equals("CHICKEN")) {
            CHICKEN++;
            if (CHICKEN > MAXCOUNT) {
                chikens.add(entity);
            }
        }
        if (entity.getType().name().equals("COW")) {
            COW++;
            if (COW > MAXCOUNT) {
                cows.add(entity);
            }
        }
        if (entity.getType().name().equals("SHEEP")) {
            SHEEP++;
            if (SHEEP > MAXCOUNT) {
                sheeps.add(entity);
            }
        }
    }

    static World world = Bukkit.getWorld("world");

    @Override
    public void run()
    {
        if (!taskActive) {

            ArrayList<Chunk> chunks = new ArrayList<>();

            for (Entity entity : world.getEntities()) {
                if (entity.getType().name().equals("PIG") || entity.getType().name().equals("CHICKEN") || entity.getType().name().equals("COW") || entity.getType().name().equals("SHEEP")) {
                    chunks.add(entity.getChunk());
                }
            }

            BukkitScheduler scheduler = BetterPerformance.getInstance().getServer().getScheduler();
            scheduler.runTaskAsynchronously(BetterPerformance.getInstance(), () -> {
                for (Chunk chunk : chunks) {
                    PIG = 0;
                    CHICKEN = 0;
                    COW = 0;
                    SHEEP = 0;

                    CheckerEntities.MAXCOUNT = BetterPerformance.getInstance().getConfig().getInt("maxEntitiesChunk");

                    for (Entity entity : chunk.getEntities()) {
                        checkEntity(entity);
                    }

                    if (!CheckerEntities.pigs.isEmpty() || !CheckerEntities.chikens.isEmpty() || !CheckerEntities.cows.isEmpty() || !CheckerEntities.sheeps.isEmpty()) {
                        taskActive = true;
                        BetterPerformance.getInstance().getLogger().log(Level.WARNING, "Many entities!");
                        BetterPerformance.getInstance().getLogger().log(Level.INFO, "Started deleting...");
                        new ClearEntities().runTaskTimer(BetterPerformance.getInstance(), 0L, 5);
                        break;
                    }
                    CheckerEntities.MAXCOUNT = BetterPerformance.getInstance().getConfig().getInt("maxEntities");

                    if (world.getChunkAt(chunk.getX()+1, chunk.getZ()).isLoaded()) {
                        for (Entity entity : world.getChunkAt(chunk.getX()+1, chunk.getZ()).getEntities()) {
                            checkEntity(entity);
                        }
                    }

                    if (world.getChunkAt(chunk.getX(), chunk.getZ()+1).isLoaded()) {
                        for (Entity entity : world.getChunkAt(chunk.getX(), chunk.getZ()+1).getEntities()) {
                            checkEntity(entity);
                        }
                    }

                    if (world.getChunkAt(chunk.getX()+1, chunk.getZ()+1).isLoaded()) {
                        for (Entity entity : world.getChunkAt(chunk.getX()+1, chunk.getZ()+1).getEntities()) {
                            checkEntity(entity);
                        }
                    }

                    if (world.getChunkAt(chunk.getX()-1, chunk.getZ()).isLoaded()) {
                        for (Entity entity : world.getChunkAt(chunk.getX()-1, chunk.getZ()).getEntities()) {
                            checkEntity(entity);
                        }
                    }

                    if (world.getChunkAt(chunk.getX(), chunk.getZ()-1).isLoaded()) {
                        for (Entity entity : world.getChunkAt(chunk.getX(), chunk.getZ()-1).getEntities()) {
                            checkEntity(entity);
                        }
                    }

                    if (world.getChunkAt(chunk.getX()-1, chunk.getZ()-1).isLoaded()) {
                        for (Entity entity : world.getChunkAt(chunk.getX()-1, chunk.getZ()-1).getEntities()) {
                            checkEntity(entity);
                        }
                    }

                    if (!CheckerEntities.pigs.isEmpty() || !CheckerEntities.chikens.isEmpty() || !CheckerEntities.cows.isEmpty() || !CheckerEntities.sheeps.isEmpty()) {
                        taskActive = true;
                        BetterPerformance.getInstance().getLogger().log(Level.WARNING, "Many entities!");
                        BetterPerformance.getInstance().getLogger().log(Level.INFO, "Started deleting...");
                        new ClearEntities().runTaskTimer(BetterPerformance.getInstance(), 0L, 5);
                        break;
                    }
                }

            });



/*
            for (Chunk chunk : world.getLoadedChunks()) {

                PIG = 0;
                CHICKEN = 0;
                COW = 0;
                SHEEP = 0;

                CheckerEntities.MAXCOUNT = BetterPerformance.getInstance().getConfig().getInt("maxEntitiesChunk");

                for (Entity entity : chunk.getEntities()) {

                    checkEntity(entity);
                }
                if (!CheckerEntities.pigs.isEmpty() || !CheckerEntities.chikens.isEmpty() || !CheckerEntities.cows.isEmpty() || !CheckerEntities.sheeps.isEmpty()) {
                    taskActive = true;
                    BetterPerformance.getInstance().getLogger().log(Level.WARNING, "Many entities!");
                    BetterPerformance.getInstance().getLogger().log(Level.INFO, "Started deleting...");
                    new ClearEntities().runTaskTimer(BetterPerformance.getInstance(), 0L, 5);
                    break;
                }
                CheckerEntities.MAXCOUNT = BetterPerformance.getInstance().getConfig().getInt("maxEntities");

                if (world.getChunkAt(chunk.getX()+1, chunk.getZ()).isLoaded()) {
                    for (Entity entity : world.getChunkAt(chunk.getX()+1, chunk.getZ()).getEntities()) {
                        checkEntity(entity);
                    }
                }

                if (world.getChunkAt(chunk.getX(), chunk.getZ()+1).isLoaded()) {
                    for (Entity entity : world.getChunkAt(chunk.getX(), chunk.getZ()+1).getEntities()) {
                        checkEntity(entity);
                    }
                }

                if (world.getChunkAt(chunk.getX()+1, chunk.getZ()+1).isLoaded()) {
                    for (Entity entity : world.getChunkAt(chunk.getX()+1, chunk.getZ()+1).getEntities()) {
                        checkEntity(entity);
                    }
                }

                if (world.getChunkAt(chunk.getX()-1, chunk.getZ()).isLoaded()) {
                    for (Entity entity : world.getChunkAt(chunk.getX()-1, chunk.getZ()).getEntities()) {
                        checkEntity(entity);
                    }
                }

                if (world.getChunkAt(chunk.getX(), chunk.getZ()-1).isLoaded()) {
                    for (Entity entity : world.getChunkAt(chunk.getX(), chunk.getZ()-1).getEntities()) {
                        checkEntity(entity);
                    }
                }

                if (world.getChunkAt(chunk.getX()-1, chunk.getZ()-1).isLoaded()) {
                    for (Entity entity : world.getChunkAt(chunk.getX()-1, chunk.getZ()-1).getEntities()) {
                        checkEntity(entity);
                    }
                }

                if (!CheckerEntities.pigs.isEmpty() || !CheckerEntities.chikens.isEmpty() || !CheckerEntities.cows.isEmpty() || !CheckerEntities.sheeps.isEmpty()) {
                    taskActive = true;
                    BetterPerformance.getInstance().getLogger().log(Level.WARNING, "Many entities!");
                    BetterPerformance.getInstance().getLogger().log(Level.INFO, "Started deleting...");
                    new ClearEntities().runTaskTimer(BetterPerformance.getInstance(), 0L, 5);
                    break;
                }

            }*/
        } else {
            BetterPerformance.getInstance().getLogger().log(Level.WARNING, "There is an active task");
        }



    }
}