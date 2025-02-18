package akia.spawnManager.command;

import akia.spawnManager.Main;
import akia.spawnManager.builder.LocationBuilder;
import akia.spawnManager.manager.CooldownManager;
import akia.spawnManager.manager.LocationManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LocationCommands implements TabExecutor {

    private final LocationManager locationManager;
    private final CooldownManager cooldownManager;

    public LocationCommands(LocationManager locationManager) {
        this.locationManager = locationManager;
        // Lit le cooldown depuis la config (valeur en secondes, par défaut 10 sec)
        long cooldownSeconds = Main.getInstance().getConfig().getLong("settings.teleport_delay", 10);
        this.cooldownManager = new CooldownManager(Main.getInstance(), cooldownSeconds);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        switch (cmd.getName().toLowerCase()) {
            case "spawn" -> handleSpawnCommand(sender, args);
            case "location" -> handleLocationCommand(sender, args);
            default -> sender.sendMessage(Component.text("Commande inconnue.", NamedTextColor.RED));
        }

        return true;
    }

    /**
     * Gestion de la commande /spawn (accessible à tous avec cooldown).
     */
    private void handleSpawnCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Seul un joueur peut utiliser cette commande.", NamedTextColor.RED));
            return;
        }

        if (cooldownManager.isOnCooldown(player)) {
            long remaining = cooldownManager.getRemainingTime(player);
            player.sendMessage(Component.text("Veuillez patienter " + ((remaining / 1000) + 1)
                    + " seconde(s) avant de réutiliser /spawn.", NamedTextColor.RED));
            return;
        }

        // Récupère la location "spawn" (doit avoir été créée via /location create/set spawn)
        LocationBuilder spawnLocation = locationManager.getLocation(Main.getInstance().getConfig().getString("settings.location_spawn", "spawn"));
        if (spawnLocation == null) {
            player.sendMessage(Component.text("La location de spawn n'est pas définie.", NamedTextColor.RED));
            return;
        }

        spawnLocation.teleportLocation(player);
        cooldownManager.startCooldown(player);
        player.sendMessage(Component.text("Téléportation vers le spawn...", NamedTextColor.GREEN));
    }

    /**
     * Gestion de la commande /location avec ses sous-commandes.
     */
    private void handleLocationCommand(@NotNull CommandSender sender, String[] args) {
        if (!sender.hasPermission("spawnmanager.location.use")) {
            sender.sendMessage(Component.text("Vous n'avez pas la permission d'utiliser cette commande.", NamedTextColor.RED));
            return;
        }
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return;
        }
        String subCommand = args[0].toLowerCase();

        // Sous-commandes réservées aux admins
        if (subCommand.equals("admin")) {
            if (!sender.hasPermission("spawnmanager.admin.use")) {
                sender.sendMessage(Component.text("Vous n'avez pas la permission d'utiliser les commandes admin.", NamedTextColor.RED));
                return;
            }
            if (args.length < 2) {
                sender.sendMessage(Component.text("Usage: /location admin <save|load>", NamedTextColor.RED));
                return;
            }
            String adminSub = args[1].toLowerCase();
            switch (adminSub) {
                case "save" -> {
                    if (!sender.hasPermission("spawnmanager.admin.save")) {
                        sender.sendMessage(Component.text("Vous n'avez pas la permission d'enregistrer les locations.", NamedTextColor.RED));
                        return;
                    }
                    locationManager.saveLocations(Main.getInstance());
                    sender.sendMessage(Component.text("Sauvegarde des locations effectuée.", NamedTextColor.GREEN));
                }
                case "load" -> {
                    if (!sender.hasPermission("spawnmanager.admin.load")) {
                        sender.sendMessage(Component.text("Vous n'avez pas la permission de charger les locations.", NamedTextColor.RED));
                        return;
                    }
                    locationManager.loadLocations(Main.getInstance());
                    sender.sendMessage(Component.text("Chargement des locations effectué.", NamedTextColor.GREEN));
                }
                default -> sender.sendMessage(Component.text("Sous-commande admin inconnue. Utilisez: save, load", NamedTextColor.RED));
            }
            return;
        }

        // Commandes standards de /location
        switch (subCommand) {
            case "create" -> {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Component.text("Seul un joueur peut exécuter cette commande.", NamedTextColor.RED));
                    return;
                }
                if (!sender.hasPermission("spawnmanager.location.create")) {
                    sender.sendMessage(Component.text("Vous n'avez pas la permission de créer des locations.", NamedTextColor.RED));
                    return;
                }
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /location create <nom>", NamedTextColor.RED));
                    return;
                }
                String name = args[1];
                Player player = (Player) sender;
                Location location = player.getLocation();
                if (locationManager.getLocation(name) != null) {
                    sender.sendMessage(Component.text("Une location avec ce nom existe déjà.", NamedTextColor.RED));
                    return;
                }
                LocationBuilder lb = new LocationBuilder(location, name);
                locationManager.registerLocation(lb);
                sender.sendMessage(Component.text("Location '" + name + "' créée à votre position.", NamedTextColor.GREEN));
            }
            case "set" -> {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Component.text("Seul un joueur peut exécuter cette commande.", NamedTextColor.RED));
                    return;
                }
                if (!sender.hasPermission("spawnmanager.location.set")) {
                    sender.sendMessage(Component.text("Vous n'avez pas la permission de modifier des locations.", NamedTextColor.RED));
                    return;
                }
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /location set <nom>", NamedTextColor.RED));
                    return;
                }
                String name = args[1];
                Player player = (Player) sender;
                LocationBuilder lb = locationManager.getLocation(name);
                if (lb == null) {
                    sender.sendMessage(Component.text("Aucune location trouvée avec ce nom.", NamedTextColor.RED));
                    return;
                }
                lb.setLocation(player.getLocation());
                sender.sendMessage(Component.text("Location '" + name + "' mise à jour avec votre position.", NamedTextColor.GREEN));
            }
            case "delete" -> {
                if (!sender.hasPermission("spawnmanager.location.delete")) {
                    sender.sendMessage(Component.text("Vous n'avez pas la permission de supprimer des locations.", NamedTextColor.RED));
                    return;
                }
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /location delete <nom>", NamedTextColor.RED));
                    return;
                }
                String name = args[1];
                if (locationManager.getLocation(name) == null) {
                    sender.sendMessage(Component.text("Aucune location trouvée avec ce nom.", NamedTextColor.RED));
                    return;
                }
                locationManager.unregisterLocation(name);
                sender.sendMessage(Component.text("Location '" + name + "' supprimée.", NamedTextColor.GREEN));
            }
            case "teleport" -> {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Component.text("Seul un joueur peut exécuter cette commande.", NamedTextColor.RED));
                    return;
                }
                if (!sender.hasPermission("spawnmanager.location.teleport")) {
                    sender.sendMessage(Component.text("Vous n'avez pas la permission de vous téléporter.", NamedTextColor.RED));
                    return;
                }
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /location teleport <nom>", NamedTextColor.RED));
                    return;
                }
                String name = args[1];
                if (!locationManager.teleportPlayer((Player) sender, name)) {
                    sender.sendMessage(Component.text("Aucune location trouvée avec ce nom.", NamedTextColor.RED));
                } else {
                    sender.sendMessage(Component.text("Téléportation en cours vers '" + name + "'.", NamedTextColor.GREEN));
                }
            }
            case "list" -> {
                if (!sender.hasPermission("spawnmanager.location.list")) {
                    sender.sendMessage(Component.text("Vous n'avez pas la permission de lister les locations.", NamedTextColor.RED));
                    return;
                }
                Collection<LocationBuilder> locations = locationManager.getAllLocations();
                if (locations.isEmpty()) {
                    sender.sendMessage(Component.text("Aucune location enregistrée.", NamedTextColor.RED));
                } else {
                    sender.sendMessage(Component.text("Locations disponibles :", NamedTextColor.GOLD));
                    for (LocationBuilder lb : locations) {
                        sender.sendMessage(Component.text("- " + lb.getName(), NamedTextColor.YELLOW));
                    }
                }
            }
            default -> sender.sendMessage(Component.text("Commande inconnue. Utilisez /location help pour la liste des commandes.", NamedTextColor.RED));
        }
    }

    /**
     * Affiche l'aide pour /location et /spawn.
     */
    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.text("Utilisation des commandes :", NamedTextColor.GOLD));
        sender.sendMessage(Component.text("/location create <nom> - Crée une location à votre position.", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/location set <nom> - Met à jour une location existante à votre position.", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/location delete <nom> - Supprime une location.", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/location teleport <nom> - Téléporte vers une location.", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/location list - Liste toutes les locations.", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/location admin save - Sauvegarde les locations.", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/location admin load - Charge les locations.", NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/spawn - Téléporte au spawn (cooldown " + (cooldownManager.getRemainingTime((Player)sender)/1000 + 1)
                + " sec).", NamedTextColor.YELLOW));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        String commandName = cmd.getName().toLowerCase();
        List<String> completions = new ArrayList<>();

        if (commandName.equals("spawn")) {
            return completions;
        } else if (commandName.equals("location")) {
            if (args.length == 1) {
                List<String> subCommands = List.of("create", "set", "delete", "teleport", "list", "help", "admin");
                String current = args[0].toLowerCase();
                for (String sub : subCommands) {
                    if (sub.startsWith(current)) {
                        completions.add(sub);
                    }
                }
            } else if (args.length == 2) {
                String subCommand = args[0].toLowerCase();
                if (subCommand.equals("delete") || subCommand.equals("set") || subCommand.equals("teleport")) {
                    String current = args[1].toLowerCase();
                    for (LocationBuilder lb : locationManager.getAllLocations()) {
                        if (lb.getName().toLowerCase().startsWith(current)) {
                            completions.add(lb.getName());
                        }
                    }
                } else if (subCommand.equals("admin")) {
                    List<String> adminSubs = List.of("save", "load");
                    String current = args[1].toLowerCase();
                    for (String sub : adminSubs) {
                        if (sub.startsWith(current)) {
                            completions.add(sub);
                        }
                    }
                }
            }
        }
        return completions;
    }
}
