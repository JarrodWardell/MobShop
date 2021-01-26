package mobshop;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Bukkit;

final class DataHandler { // this shouldn't need to be extended. replace it if you want to use it in some other way
    private FileConfiguration yamlData = new YamlConfiguration();
    private File file;

    DataHandler(String path) { // ensure the data file actually exists
        file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {}
        }
        try {
            yamlData.load(file);
        } catch (Exception e) {}
    }

    protected  Boolean save(HashMap<UUID, Integer> data) { // if using a database, could save per-user assuming their data was changed (keep a list of all modified values, then during the async save function loop through all the changed data and push to the database?) would be better, but I'm just using a file for player data here so I need to save/load the whole thing at once anyways.
        for (Map.Entry<UUID, Integer> entry : data.entrySet()) {
            yamlData.set(entry.getKey() + "", entry.getValue());
        }
        try {
            yamlData.save(file);
        } catch (Exception e) {
            Bukkit.getLogger().severe("Error saving user data!\n\n" + e);
            return false;
        }
        return true;
    }

    protected HashMap<UUID, Integer> load() { // if using a database, a more efficient route would be to load a players data as they join, rather than everyones at the start. doing it this way while using a file, though, cuts down on disk usage.
        HashMap<UUID, Integer> values = new HashMap<UUID, Integer>();
        for (String id : yamlData.getConfigurationSection("").getKeys(false)) {
            values.put(UUID.fromString(id), yamlData.getInt(id));
        }
        return values;
    }
}
