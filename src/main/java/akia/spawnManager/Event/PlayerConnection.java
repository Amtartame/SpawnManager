package akia.spawnManager.Event;

import akia.spawnManager.Main;
import akia.spawnManager.builder.LocationBuilder;
import akia.spawnManager.manager.LocationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * The PlayerConnection class is responsible for managing player connection events
 * and performing related actions such as teleporting players to specific locations
 * when they join the server. This class implements the {@link Listener} interface
 * to respond to Bukkit events.
 */
public class PlayerConnection implements Listener {

    /**
     * Manages player location-related actions and functionalities for the associated class.
     * This variable holds the {@link LocationManager} instance used to handle location
     * registrations, retrievals, manipulations, and player teleportation functionalities.
     * It serves as the primary point of accessing and managing in-game locations, ensuring
     * consistent handling throughout the class where it is utilized.
     *
     * The {@code locationManager} is responsible for:
     * - Storing and retrieving locations defined in a configuration file or dynamically.
     * - Registering, unregistering, and managing named in-game locations.
     * - Facilitating player teleportation to specified locations.
     * - Maintaining a cache of {@link LocationBuilder} objects for efficient location management.
     *
     * This instance is initialized via the constructor and remains immutable (final)
     * throughout the lifecycle of the containing class.
     */
    private final LocationManager locationManager;

    /**
     * Initializes a PlayerConnection instance with the specified LocationManager.
     * This constructor establishes a dependency on the LocationManager, enabling
     * interaction with and management of player-related location events.
     *
     * @param locationManager the LocationManager instance used to manage locations. Must not be null.
     */
    public PlayerConnection(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    /**
     * Handles the event when a player joins the server. This method retrieves the spawn
     * location defined in the plugin configuration, teleports the player to the specified
     * location, and sends them a notification message. If no spawn location is defined,
     * an error message is sent to the player.
     *
     * @param event the event triggered when a player joins the server
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Récupère le nom de la location de spawn défini dans la config
        String spawnLocationName = Main.getInstance().getConfig().getString("spawn.location", "spawn");
        // Récupère la LocationBuilder associée
        LocationBuilder spawnLocation = locationManager.getLocation(spawnLocationName);
        if (spawnLocation != null) {
            spawnLocation.teleportLocation(player);
            player.sendMessage(Component.text("Vous avez été téléporté au spawn.", NamedTextColor.GREEN));
        } else {
            player.sendMessage(Component.text("Le spawn n'est pas défini. Veuillez contacter un administrateur.", NamedTextColor.RED));
        }
    }
}
