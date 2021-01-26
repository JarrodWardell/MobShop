package mobshop;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.HashMap;
import java.util.List;

/*
    Current Known Issues:
        - Config generates with "ZombieVillager" rather than "VillagerZombie". Weird quirk with how they named entity classes? Not a large issue, anyways. Temporarily solved through a bodge. Figure this out later!
*/

public class Mobshop extends JavaPlugin {
    final private String dataFile = getDataFolder().getAbsolutePath() + "/players.yml";
    private FileConfiguration config = getConfig();
    protected HashMap<UUID, Integer> userData = new HashMap<UUID, Integer>();
    private DataHandler dataHandler = new DataHandler(dataFile);
    private ShopInventoryEvents shopInv;
    private EditShopEvents editInv;

    @Override
    public void onEnable() {
        config.addDefault("save.saveFrequency", 15); // save frequency in tickminutes (what the server sees as minutes based on ticks)
        for (EntityType entity : EntityType.values()) { // create default config of 0% droprate for 0 coins for every living entity
            if (entity.isAlive() && !entity.getEntityClass().getName().substring(entity.getEntityClass().getName().lastIndexOf(".") + 1).replace("Craft", "").equals("ZombieVillager")) {
                config.addDefault("mob." + entity.getEntityClass().getName().substring(entity.getEntityClass().getName().lastIndexOf(".") + 1).replace("Craft", "") + ".dropChance", 0);
                config.addDefault("mob." + entity.getEntityClass().getName().substring(entity.getEntityClass().getName().lastIndexOf(".") + 1).replace("Craft", "") + ".maxDrop", 0);
            }
        }
        config.addDefault("mob.VillagerZombie.dropChance", 0);
        config.addDefault("mob.VillagerZombie.maxDrop", 0); // for some reason this ONE entity in 1.12.2 has its classes named differently in EntityType and from Entity Events.

        config.addDefault("shop.displayName", "Mob Shop");
        config.addDefault("shop.size", 27);
        config.addDefault("shop.contents", Bukkit.createInventory(null, 27).getContents());
        config.options().copyDefaults(true);
        saveConfig();
        
        Plugin plugin = this;
        userData = dataHandler.load();

        Inventory toSet = Bukkit.createInventory(null, config.getInt("shop.size"), config.getString("shop.displayName"));
        
        List<ItemStack> contents = (List<ItemStack>)config.getList("shop.contents"); // living under the assumption the person configuring it is not stupid with config. if this fails they messed up their config anywho

        toSet.setContents(config.getList("shop.contents") == null ? Bukkit.createInventory(null, 27).getContents() : contents.toArray(new ItemStack[0]));

        shopInv = new ShopInventoryEvents(toSet, this);
        editInv = new EditShopEvents(toSet, shopInv);

        getServer().getPluginManager().registerEvents(shopInv, this);
        getServer().getPluginManager().registerEvents(editInv, this);
        getServer().getPluginManager().registerEvents(new DeathEvents(config, this), this);
        getCommand("addcoins").setExecutor(new AddCommand(this));
        getCommand("coinbalance").setExecutor(new BalCommand(this));
        getCommand("removecoins").setExecutor(new RemoveCommand(this));
        getCommand("setcoins").setExecutor(new SetCommand(this));
        getCommand("givecoins").setExecutor(new GiveCommand(this));
        getCommand("coinshop").setExecutor(new ShopCommand(this, shopInv, editInv));

        new ScheduledTasks(dataHandler, this).saveData.runTaskTimerAsynchronously(plugin, config.getInt("save.saveFrequency") * 20 * 60, config.getInt("save.saveFrequency") * 20 * 60);
    }

    protected void registerEvent(Listener events) {
        getServer().getPluginManager().registerEvents(events, this);
    }

    public Integer addCoins(Player player, Integer amount) {
        if (userData.containsKey(player.getUniqueId())) {
            userData.put(player.getUniqueId(), userData.get(player.getUniqueId()) + amount);
        } else {
            userData.put(player.getUniqueId(), amount);
        }
        return userData.get(player.getUniqueId());
    }

    public boolean removeCoins(Player player, Integer amount) {
        if (userData.containsKey(player.getUniqueId())) {
            userData.put(player.getUniqueId(), userData.get(player.getUniqueId()) - amount);
            if (userData.get(player.getUniqueId()) < 0) {
                //userData.remove(player.getUniqueId()); // might be stupid to do this lol
                return false;
            }
            return true;
        }
        return false;
    }

    public void setCoins(Player player, Integer amount) {
        /*if (amount <= 0 && userData.containsKey(player.getUniqueId())) {
            userData.remove(player.getUniqueId());
        } else {*/
            userData.put(player.getUniqueId(), amount);
        //}
    }

    public Integer getCoins(Player player) {
        if (userData.containsKey(player.getUniqueId())) {
            return userData.get(player.getUniqueId());
        }
        return 0;
    }

    public boolean hasCoins(Player player, Integer amount) {
        if (userData.containsKey(player.getUniqueId()) && userData.get(player.getUniqueId()) >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        if (!dataHandler.save(userData)) {
            getLogger().severe(ChatColor.RED + "Failed to save userdata!");
        } else {
            getLogger().info("Saved user data.");
        }

        Inventory toSave = shopInv.getInventory();

        config.set("shop.displayName", toSave.getName());
        config.set("shop.size", toSave.getSize());
        config.set("shop.contents", toSave.getContents());

        saveConfig();
    }
}
