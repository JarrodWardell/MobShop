package mobshop;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import org.apache.commons.lang3.tuple.Pair;


public class Mobshop extends JavaPlugin {
    FileConfiguration config = getConfig();
    List<Pair<UUID, Integer>> userData = new ArrayList<Pair<UUID, Integer>>();

    @Override
    public void onEnable() {
        
        getServer().getPluginManager().registerEvents(new DeathEvents(), this);
    }

    public static void addCoins(Player player, Integer amount) {
        player.getUniqueId();
    }

    @Override
    public void onDisable() {
        
    }
}
