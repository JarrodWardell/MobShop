package mobshop;

import org.bukkit.scheduler.BukkitRunnable;

public class ScheduledTasks {
    private DataHandler dataHandler;
    private Mobshop mobShop;

    ScheduledTasks(DataHandler dataHandler, Mobshop mobShop) {
        this.dataHandler = dataHandler;
        this.mobShop = mobShop;
    }

    protected BukkitRunnable saveData = new BukkitRunnable() {
        @Override
        public void run() {
            mobShop.getLogger().info("Saving user data.");
            dataHandler.save(mobShop.userData);
            mobShop.getLogger().info("Saved user data.");
        }
    };
}
