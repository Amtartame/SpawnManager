package akia.spawnManager;

import akia.spawnManager.builder.LocationBuilder;
import akia.spawnManager.listener.Commands;
import akia.spawnManager.listener.Events;
import akia.spawnManager.manager.LocationManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;
    private final LocationManager locationManager = new LocationManager();

    /**
     * Called when the plugin is enabled. Initializes the plugin's components such as commands,
     * events, and location management.
     */
    @Override
    public void onEnable() {
        instance = this;

        // Initialize commands and events
        new Commands(this);
        new Events(this);

        // Load locations from storage
        locationManager.loadLocations(this);
    }

    /**
     * Called when the plugin is disabled. Saves all locations to storage to ensure data persistence.
     */
    @Override
    public void onDisable() {
        // Save locations to storage
        locationManager.saveLocations(this);
    }

    /**
     * Retrieves the instance of the Main class for accessing plugin-specific methods.
     *
     * @return the single instance of this plugin
     */
    public static Main getInstance() {
        return instance;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }
}
