package be.isach.ultracosmetics.config;

import be.isach.ultracosmetics.UltraCosmeticsData;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Settings manager.
 *
 * @author iSach
 * @since 07-21-2015
 */
public class SettingsManager {

    // Config file.
    public FileConfiguration fileConfiguration;
    private File file;
    // if the config was loaded successfully. Set to false on failure.
    private boolean success = true;

    /**
     * Creates a new file and defines fileConfiguration and file.
     *
     */
    protected SettingsManager(String fileName) {
        File folder = new File(UltraCosmeticsData.get().getPlugin().getDataFolder(), "/data");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        file = new File(UltraCosmeticsData.get().getPlugin().getDataFolder(), fileName + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                success = false;
                e.printStackTrace();
            }
        }

        // not using YamlConfiguration.loadConfiguration
        // because we want to catch the exceptions and act on them
        fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            success = false;
            e.printStackTrace();
        }

    }

    /**
     * Gets the messages SettingsManager.
     *
     * @return the messages SettingsManager.
     */
    public static CustomConfiguration getConfig() {
        return UltraCosmeticsData.get().getPlugin().getConfig();
    }

    /**
     * Gets the data settings manager of a player.
     *
     * @param p The player.
     * @return the data settings manager of a player.
     */
    public static SettingsManager getData(Player p) {
        return new SettingsManager("/data/" + p.getUniqueId());
    }

    /**
     * Gets the data settings manager of a uuid.
     *
     * @param uuid The player's {@link java.util.UUID UUID}.
     * @return the data settings manager of a uuid.
     */
    public static SettingsManager getData(UUID uuid) {
        return new SettingsManager("/data/" + uuid.toString());
    }

    public static boolean hasData(UUID uuid) {
        return Arrays.asList(UltraCosmeticsData.get().getPlugin().getDataFolder()
                .listFiles()).contains(new File(uuid.toString() + ".yml"));
    }

    public boolean success() {
        return success;
    }

    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Sets the value of a given path.
     *
     */
    public void set(String path, Object value, boolean autosave) {
        fileConfiguration.set(path, value);
        if (autosave) save();
    }

    public void set(String path, Object value) {
        set(path, value, false);
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a boolean in config from a path.
     *
     * @param path The path in config to the boolean.
     * @return The boolean found in config from the given path.
     */
    public boolean getBoolean(String path) {
        return fileConfiguration.getBoolean(path);
    }

    public boolean getBoolean(String path, boolean defaultValue) {
        return fileConfiguration.getBoolean(path, defaultValue);
    }

    /**
     * Get a int in config from a path.
     *
     * @param path The path in config to the int.
     * @return The int found in config from the given path.
     */
    public int getInt(String path) {
        return fileConfiguration.getInt(path);
    }

    /**
     * Get a double in config from a path.
     *
     * @param path The path in config to the double.
     * @return The double found in config from the given path.
     */
    public double getDouble(String path) {
        return fileConfiguration.getDouble(path);
    }

    public String getString(String path) {
        return fileConfiguration.getString(path);
    }

    public List<String> getStringList(String path) {
        return fileConfiguration.getStringList(path);
    }

    /**
     * Sets a value if the fileConfiguration doesn't contain the path.
     *
     * @param path  The fileConfiguration path.
     * @param value The value for this path.
     */
    public boolean addDefault(String path, Object value) {
        if (!fileConfiguration.contains(path)) {
            set(path, value);
            return true;
        }
        return false;
    }

    /**
     * Create and get a configuration section for a given path.
     *
     * @return the configuration section created for the given path.
     */
    public ConfigurationSection createConfigurationSection(String path) {
        return fileConfiguration.createSection(path);
    }

    public Object get(String path) {
        return fileConfiguration.get(path);
    }

    /**
     * @return {@code true} if the fileConfiguration contains the path, {@code false} otherwise.
     */
    public boolean contains(String path) {
        return fileConfiguration.contains(path);
    }

    public static boolean isAllowedWorld(World world) {
        List<String> worlds = getConfig().getStringList("Enabled-Worlds");
        if (worlds.contains("*")) return true;
        if (worlds.contains(world.getName())) return true;
        return false;
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return fileConfiguration.getConfigurationSection(path);
    }
}
