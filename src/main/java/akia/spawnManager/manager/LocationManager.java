package akia.spawnManager.manager;

import akia.spawnManager.builder.LocationBuilder;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages {@link LocationBuilder} instances, enabling the registration, retrieval,
 * manipulation, and removal of named locations. Provides functionality to store and
 * retrieve locations from a persistent configuration file and to teleport players
 * to registered locations.
 */
public class LocationManager {

    /**
     * A cache for storing {@code LocationBuilder} instances, identified by their names.
     * This map facilitates quick access, registration, retrieval, and manipulation
     * of {@code LocationBuilder} objects within the {@code LocationManager} class.
     * The key represents the name of the location, and the value is the associated
     * {@code LocationBuilder} object. Ensures efficient management of named locations.
     */
    private final Map<String, LocationBuilder> locationCache = new HashMap<>();

    /**
     * Registers a location using the provided {@code LocationBuilder}.
     * The location is stored in the internal cache using its name as the key.
     * Throws an {@code IllegalArgumentException} if the provided {@code LocationBuilder}
     * is null or its name is null.
     *
     * @param locationBuilder The {@code LocationBuilder} object containing the location
     *                        and name to register. Must not be null, and its name must also not be null.
     */
    public void registerLocation(LocationBuilder locationBuilder) {
        if (locationBuilder == null || locationBuilder.getName() == null) {
            throw new IllegalArgumentException("LocationBuilder et son nom ne peuvent être null.");
        }
        locationCache.put(locationBuilder.getName(), locationBuilder);
    }

    /**
     * Retrieves the {@link LocationBuilder} associated with the specified name from the location cache.
     *
     * @param name the name of the location to retrieve
     * @return the {@code LocationBuilder} associated with the given name,
     *         or {@code null} if no matching location is found
     */
    public LocationBuilder getLocation(String name) {
        return locationCache.get(name);
    }

    /**
     * Retrieves all registered location builders as an unmodifiable collection.
     *
     * @return a collection of {@code LocationBuilder} objects representing all locations
     *         currently registered in the location cache.
     */
    public Collection<LocationBuilder> getAllLocations() {
        return Collections.unmodifiableCollection(locationCache.values());
    }

    /**
     * Unregisters a location identified by its name from the location cache.
     *
     * @param name the name of the location to be removed from the cache. Cannot be null.
     */
    public void unregisterLocation(String name) {
        locationCache.remove(name);
    }

    /**
     * Teleports a player to a specific location based on the location name.
     * The method looks up the location using the provided name and,
     * if found, teleports the given player to that location.
     *
     * @param player The player to be teleported. Must not be null.
     * @param locationName The name of the location to teleport the player to. Must not be null.
     * @return {@code true} if the player was successfully teleported to the location,
     *         {@code false} if the location with the specified name could not be found.
     */
    public boolean teleportPlayer(Player player, String locationName) {
        LocationBuilder locationBuilder = getLocation(locationName);
        if (locationBuilder != null) {
            locationBuilder.teleportLocation(player);
            return true;
        }
        return false;
    }

    /**
     * Clears all stored locations by removing all entries from the location cache.
     * This operation will result in an empty cache, effectively resetting the managed locations.
     */
    public void clearLocations() {
        locationCache.clear();
    }

    /**
     * Saves the currently cached locations to a YAML configuration file named "locations.yml"
     * within the data folder of the specified plugin. Each location is associated with a key
     * representing its name in the cache and is serialized before being saved.
     *
     * @param plugin the plugin instance whose data folder is used to save the locations file.
     *               Must not be null.
     */
    public void saveLocations(Plugin plugin) {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File file = new File(dataFolder, "locations.yml");
        YamlConfiguration config = new YamlConfiguration();

        for (Map.Entry<String, LocationBuilder> entry : locationCache.entrySet()) {
            String key = entry.getKey();
            Location location = entry.getValue().getLocation();
            if (location != null) {
                // Bukkit se charge de sérialiser l'objet Location
                config.set("locations." + key, location);
            }
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads location data from a configuration file located in the plugin's data folder.
     * If the file does not exist, the method exits without taking any action.
     * If locations are found within the configuration, they are converted into
     * {@link LocationBuilder} objects and registered into the system.
     *
     * @param plugin the plugin whose data folder contains the locations configuration file
     */
    public void loadLocations(Plugin plugin) {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        File file = new File(dataFolder, "locations.yml");
        if (!file.exists()) {
            return;
        }

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (config.contains("locations")) {
            ConfigurationSection section = config.getConfigurationSection("locations");
            for (String key : section.getKeys(false)) {
                Location location = section.getLocation(key);
                if (location != null) {
                    LocationBuilder lb = new LocationBuilder(location, key);
                    registerLocation(lb);
                }
            }
        }
    }
}
