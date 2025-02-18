package akia.spawnManager.manager;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    /**
     * A map that manages cooldown timers for players identified by their unique UUIDs.
     * The key represents the player's UUID, and the value is the timestamp (in milliseconds)
     * at which the cooldown expires for the given player. This structure is used to track
     * and enforce cooldowns in the system, ensuring that players cannot perform certain
     * actions until their cooldown has elapsed.
     */
    private final Map<UUID, Long> cooldowns = new HashMap<>();
    /**
     * Represents the cooldown duration in milliseconds.
     * This value determines the total time a player must wait
     * between specific actions or events to prevent immediate repetition.
     * Set during the initialization of the class by converting
     * a given cooldown period in seconds to milliseconds.
     */
    private final long cooldownMillis;
    /**
     * Represents the plugin instance associated with this class.
     * This reference is primarily used to schedule tasks, manage configuration files,
     * and perform plugin-specific operations.
     * Ensures that the class has access to the plugin's context within the Bukkit ecosystem.
     */
    private final Plugin plugin;

    /**
     * Constructs a new instance of {@code CooldownManager}.
     * This manager is responsible for handling cooldown periods for players.
     *
     * @param plugin the plugin instance associated with this cooldown manager. Must not be null.
     * @param cooldownSeconds the cooldown duration in seconds. This value determines the time
     *                         players must wait before performing an action again.
     */
    public CooldownManager(Plugin plugin, long cooldownSeconds) {
        this.plugin = plugin;
        this.cooldownMillis = cooldownSeconds * 1000;
    }

    /**
     * Checks if a {@link Player} is currently on cooldown.
     * A cooldown prevents the player from performing certain actions until a specified time has passed.
     * If the cooldown has expired, the corresponding entry is removed from the internal cooldown tracking map.
     *
     * @param player the {@link Player} whose cooldown status is being checked
     * @return {@code true} if the player is still on cooldown, {@code false} if the cooldown has expired or does not exist
     */
    public boolean isOnCooldown(Player player) {
        if (player.isOp()) {
            return false;
        }
        UUID uuid = player.getUniqueId();
        if (cooldowns.containsKey(uuid)) {
            long expireTime = cooldowns.get(uuid);
            if (System.currentTimeMillis() < expireTime) {
                return true;
            } else {
                cooldowns.remove(uuid);
            }
        }
        return false;
    }

    /**
     * Retrieves the remaining time in milliseconds for a player's cooldown.
     * If the player is not on cooldown or the cooldown has expired, returns 0.
     *
     * @param player the player for whom the remaining cooldown time is being queried
     * @return the remaining cooldown time in milliseconds; returns 0 if there is
     *         no active cooldown or if the cooldown has expired
     */
    public long getRemainingTime(Player player) {
        UUID uuid = player.getUniqueId();
        if (cooldowns.containsKey(uuid)) {
            long expireTime = cooldowns.get(uuid);
            long remaining = expireTime - System.currentTimeMillis();
            return remaining > 0 ? remaining : 0;
        }
        return 0;
    }

    /**
     * Starts a cooldown for the specified player. The cooldown period is determined
     * by the pre-configured duration in milliseconds. During the cooldown, the player
     * cannot perform certain actions until the cooldown expires. Once the cooldown
     * ends, the player is removed from the cooldown map automatically.
     *
     * @param player the player for whom the cooldown should be started. Must not be null.
     */
    public void startCooldown(Player player) {
        UUID uuid = player.getUniqueId();
        long expireTime = System.currentTimeMillis() + cooldownMillis;
        cooldowns.put(uuid, expireTime);

        // Utilise le BukkitScheduler pour supprimer l'entrée après cooldownMillis (1 tick = 50 ms)
        new BukkitRunnable() {
            @Override
            public void run() {
                cooldowns.remove(uuid);
            }
        }.runTaskLater(plugin, cooldownMillis / 50);
    }
}
