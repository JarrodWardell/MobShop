package mobshop;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.HashMap;


public class Mobshop extends JavaPlugin {
    FileConfiguration config = getConfig();
    private static HashMap<UUID, Integer> userData = new HashMap<UUID, Integer>();

    @Override
    public void onEnable() {
        
        getServer().getPluginManager().registerEvents(new DeathEvents(), this);
        getCommand("addcoins").setExecutor(new AddCommand());
        getCommand("coinbalance").setExecutor(new BalCommand());
        getCommand("removecoins").setExecutor(new AddCommand());
        getCommand("setcoins").setExecutor(new BalCommand());
        getCommand("givecoins").setExecutor(new BalCommand());
    }

    public static Integer addCoins(Player player, Integer amount) {
        if (userData.containsKey(player.getUniqueId())) {
            userData.put(player.getUniqueId(), userData.get(player.getUniqueId()) + amount);
        } else {
            userData.put(player.getUniqueId(), amount);
        }
        return userData.get(player.getUniqueId());
    }

    public static boolean removeCoins(Player player, Integer amount) {
        if (userData.containsKey(player.getUniqueId())) {
            userData.put(player.getUniqueId(), userData.get(player.getUniqueId()) - amount);
            if (userData.get(player.getUniqueId()) < 0) {
                userData.remove(player.getUniqueId());
                return false;
            }
            return true;
        }
        return false;
    }

    public static void setCoins(Player player, Integer amount) {
        if (amount <= 0 && userData.containsKey(player.getUniqueId())) {
            userData.remove(player.getUniqueId());
        } else {
            userData.put(player.getUniqueId(), amount);
        }
    }

    public static Integer getCoins(Player player) {
        if (userData.containsKey(player.getUniqueId())) {
            return userData.get(player.getUniqueId());
        }
        return 0;
    }

    public static boolean hasCoins(Player player, Integer amount) {
        if (userData.containsKey(player.getUniqueId()) && userData.get(player.getUniqueId()) >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        
    }
}
