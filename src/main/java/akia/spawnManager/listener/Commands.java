package akia.spawnManager.listener;

import akia.spawnManager.Main;
import akia.spawnManager.command.LocationCommands;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Commands {

    /**
     * Initializes the Commands class and registers the commands for the plugin.
     *
     * @param main the instance of the Main class, used to access commands
     */
    public Commands(@NotNull Main main) {
        registerCommands(main);
    }

    /**
     * Registers command executors for the specified main plugin instance. This method ensures that
     * commands "spawn" and "location" are associated with their respective executors.
     *
     * @param main the main plugin instance, must not be null
     * @throws NullPointerException if the main instance or its associated commands are null
     */
    private void registerCommands(@NotNull Main main) {
        Objects.requireNonNull(main.getCommand("spawn")).setExecutor(new LocationCommands(Main.getInstance().getLocationManager()));
        Objects.requireNonNull(main.getCommand("location")).setExecutor(new LocationCommands(Main.getInstance().getLocationManager()));

    }

}
