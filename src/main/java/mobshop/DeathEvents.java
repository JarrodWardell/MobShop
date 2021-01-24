package mobshop;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Random;

final class DeathEvents implements Listener {
    private static FileConfiguration config;
    private static Random random = new Random();

    public static void updateConfig(FileConfiguration c) {
        config = c;
    }

    @EventHandler
    public static void onMobDie(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Double dropChance = config.getDouble(event.getEntity().getClass().getName() + "-dropChance");
            Integer maxDrop = config.getInt(event.getEntity().getClass().getName() + "-maxDrop");
            Integer drop = 0;
            for (Integer i = 0; i < maxDrop; i++) {
                if (random.nextDouble() * 100 <= dropChance) {
                    drop++;
                }
            }
            Mobshop.addCoins(event.getEntity().getKiller(), drop);
        }
    }
}
