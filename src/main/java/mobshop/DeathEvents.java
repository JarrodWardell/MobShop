package mobshop;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.ChatColor;

import java.util.Random;

final class DeathEvents implements Listener {
    private FileConfiguration config;
    private Random random = new Random();
    private Mobshop mobShop;

    DeathEvents(FileConfiguration c, Mobshop mobShop) {
        config = c;
        this.mobShop = mobShop;
    }

    @EventHandler
    public void onMobDie(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            //Bukkit.getLogger().info(event.getEntity().getClass().getName().substring(event.getEntity().getClass().getName().lastIndexOf(".") + 1).replace("Craft", ""));
            if (config.get("mob." + event.getEntity().getClass().getName().substring(event.getEntity().getClass().getName().lastIndexOf(".") + 1).replace("Craft", "") + ".dropChance") == null || config.get("mob." + event.getEntity().getClass().getName().substring(event.getEntity().getClass().getName().lastIndexOf(".") + 1).replace("Craft", "") + ".maxDrop") == null) {
                Bukkit.getLogger().warning(ChatColor.RED + "Entity '" + event.getEntity().getClass().getName().substring(event.getEntity().getClass().getName().lastIndexOf(".") + 1).replace("Craft", "") + "' does not have a proper config entry!");
                return;
            }
            Double dropChance = config.getDouble("mob." + event.getEntity().getClass().getName().substring(event.getEntity().getClass().getName().lastIndexOf(".") + 1).replace("Craft", "") + ".dropChance");
            Integer maxDrop = config.getInt("mob." + event.getEntity().getClass().getName().substring(event.getEntity().getClass().getName().lastIndexOf(".") + 1).replace("Craft", "") + ".maxDrop");
            Integer drop = 0;
            for (Integer i = 0; i < maxDrop; i++) {
                if (random.nextDouble() * 100 <= dropChance) {
                    drop++;
                }
            }
            mobShop.addCoins(event.getEntity().getKiller(), drop);
        }
    }
}
