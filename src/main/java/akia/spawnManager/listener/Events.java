package akia.spawnManager.listener;

import akia.spawnManager.Event.PlayerConnection;
import akia.spawnManager.Main;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class Events {

    /**
     * The `pluginManager` is responsible for managing plugins within the server environment.
     * It allows the registration, interaction, and management of event listeners and commands.
     *
     * This instance is initialized using the server's plugin manager provided by the main plugin class
     * and is utilized to register events and facilitate interactions between the plugin and the server.
     */
    private final PluginManager pluginManager;

    /**
     * Initializes and registers game events by handling the plugin's main class.
     *
     * @param main the instance of the Main class used to access the plugin's server and plugin manager
     */
    public Events(@NotNull Main main) {
        this.pluginManager = main.getServer().getPluginManager();
        registerPlayerEvents(main);
    }

    /**
     * Registers player-related events, linking them to the main plugin instance.
     * This method uses the {@link PluginManager} to associate the {@link PlayerConnection} event listener
     * with the provided plugin, enabling the plugin to respond to player events such as connections.
     *
     * @param main The instance of the {@link Main} plugin used to register event listeners. Must not be null.
     */
    private void registerPlayerEvents(Main main) {
        getPluginManager().registerEvents(new PlayerConnection(Main.getInstance().getLocationManager()), main);
    }

    /**
     * Retrieves the plugin manager associated with the server.
     *
     * @return the {@link PluginManager} responsible for handling plugin events and interactions
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }
}
