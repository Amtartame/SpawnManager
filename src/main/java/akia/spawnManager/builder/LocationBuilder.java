package akia.spawnManager.builder;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * Represents a builder for managing {@code Location} objects with associated names.
 * This class provides mechanisms to retrieve and update the location and name,
 * teleport a player, and compare instances based on the name.
 */
public class LocationBuilder {

    /**
     * Represents the {@code Location} associated with this builder.
     * The location can be retrieved or updated using appropriate methods,
     * and is used as a reference point for player teleportation and other operations.
     */
    private Location location;
    /**
     * Represents the name associated with the {@code Location} managed by the
     * {@code LocationBuilder}. This name is used for identification purposes
     * and can be retrieved, updated, and compared in relation to other instances
     * of {@code LocationBuilder}.
     */
    private String name;

    /**
     * Constructs a new {@code LocationBuilder} with the specified {@code Location} and name.
     *
     * @param location the {@code Location} to be associated with this builder instance
     * @param name the name to be associated with this builder instance
     */
    public LocationBuilder(Location location, String name) {
        this.location = location;
        this.name = name;
    }

    /**
     * Retrieves the current {@code Location} associated with this instance.
     *
     * @return the {@code Location} object representing the current location
     *         stored in this instance, or {@code null} if no location is set.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Retrieves the name associated with the current instance.
     *
     * @return the name of the {@code LocationBuilder}, or {@code null} if no name has been set.
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the current location of the {@code LocationBuilder}.
     *
     * @param location the new {@code Location} to be set; may replace the existing location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Sets the name associated with this object.
     *
     * @param name the new name to be assigned
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Teleports the specified player to the current location of this {@code LocationBuilder}.
     * The teleportation will occur only if both the location and player are non-null.
     *
     * @param player The player to be teleported. Must not be null.
     */
    public void teleportLocation(Player player) {
        if (location != null && player != null) {
            player.teleport(location);
        }
    }

    /**
     * Compares the specified object with this {@code LocationBuilder} for equality.
     * Returns {@code true} if the given object is also a {@code LocationBuilder}
     * and the {@code name} fields of both objects are equal.
     *
     * @param o the object to be compared for equality with this {@code LocationBuilder}
     * @return {@code true} if the specified object is equal to this {@code LocationBuilder}, otherwise {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationBuilder that)) return false;
        return Objects.equals(name, that.name);
    }

    /**
     * Returns a string representation of the {@code LocationBuilder} instance,
     * including the location and name fields.
     *
     * @return A string representation of this {@code LocationBuilder} instance.
     */
    @Override
    public String toString() {
        return String.format("LocationBuilder{location=%s, name='%s'}", location, name);
    }
}
