# SpawnManager 🏠✨

Bienvenue sur **SpawnManager** – le plugin ultime pour la gestion des spawns et des téléportations sur votre serveur Minecraft ! 🚀  
Organisez, configurez et gérez facilement les points de spawn et de téléportation avec une flexibilité totale. ⚡️

---

## Table des matières 📚  
- [Aperçu](#aperçu)  
- [Fonctionnalités](#fonctionnalités)  
- [Technologies & Dépendances](#technologies--dépendances)  
- [Installation](#installation)  
- [Configuration](#configuration)  
- [Utilisation](#utilisation)  
- [Dépannage](#dépannage)  
- [Contribuer](#contribuer)  
- [Licence](#licence)  

---

## Aperçu 🌟

**SpawnManager** permet de :  
- **Définir et gérer des points de spawn personnalisés** pour les joueurs et les groupes.  
- **Configurer un cooldown et une temporisation** pour les téléportations.  
- **Restreindre l’accès aux commandes** via des permissions spécifiques.  
- **Sauvegarder les spawns de manière persistante** dans un fichier `config.yml`.  

Chaque joueur peut être téléporté en toute sécurité au spawn défini par les administrateurs, garantissant une expérience fluide et contrôlée. 😊

---

## Fonctionnalités ✨

- **Gestion avancée des spawns**
  - Définissez un spawn global accessible à tous.
  - Créez plusieurs points de spawn pour des groupes ou mondes spécifiques.

- **Téléportation contrôlée**
  - Ajoutez un **cooldown** configurable avant la téléportation.
  - Appliquez une **temporisation** pour éviter les abus.

- **Système de permissions intégré**
  - Différents niveaux d'accès aux commandes (`spawnmanager.spawn.use`, `spawnmanager.location.create`, etc.).

- **Sauvegarde automatique des données**
  - Les spawns sont stockés et restaurés à chaque redémarrage.

---

## Technologies & Dépendances 🛠️

- **Java 17+** – Langage principal du plugin ☕
- **Spigot/Paper API (Minecraft 1.21)** – Compatibilité serveur 🧱
- **YAML** – Configuration et persistance des données 📄

### Exemple de dépendance Maven
```xml
<dependencies>
  <!-- Spigot API -->
  <dependency>
    <groupId>org.spigotmc</groupId>
    <artifactId>spigot-api</artifactId>
    <version>1.21-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
```

---

## Installation 🚀

1. **Télécharger ou compiler le plugin**  
   - Clonez le repo ou téléchargez la dernière version.  
   - Compilez avec Maven (si nécessaire).

2. **Déployer le JAR**  
   - Placez `SpawnManager.jar` dans le dossier `plugins/` de votre serveur.

3. **Démarrer le serveur**  
   - Lancez le serveur et laissez le plugin générer la configuration par défaut.

---

## Configuration ⚙️

Personnalisez le plugin dans `config.yml`.

```yaml
settings:
  teleport_delay: 5  # Délai avant téléportation (secondes)
  location_spawn: "spawn" # Nom de la location du spawn global
```

---

## Utilisation 📜

- **Commandes principales :**  
  - `/spawn` – Téléporte au spawn défini.  
  - `/location create <nom>` – Crée une location à votre position actuelle.  
  - `/location set <nom>` – Met à jour une location existante avec votre position actuelle.  
  - `/location delete <nom>` – Supprime une location.  
  - `/location teleport <nom>` – Téléporte un joueur vers une location définie.  
  - `/location list` – Liste toutes les locations disponibles.  
  - `/location admin save` – Sauvegarde les locations en cours.  
  - `/location admin load` – Charge les locations sauvegardées.  
  - `/location help` – Affiche l’aide des commandes `/location`.

- **Exemple d'utilisation :**
  - `/spawn` → Téléportation vers le spawn global.
  - `/location create home` → Crée une location nommée "home" à votre position.
  - `/location teleport home` → Téléporte le joueur vers la location "home".

---

## Dépannage ⚠️

- **Problèmes de permissions ?**  
  Vérifiez que votre système de permissions (LuckPerms, PermissionsEx) est bien configuré.

- **Spawn non défini ?**  
  Utilisez `/location create spawn` pour définir une location de spawn avant d'exécuter `/spawn`.

- **Erreur au démarrage ?**  
  Assurez-vous que votre `config.yml` est correctement formaté.

---

## Contribuer 🤝

Les contributions sont les bienvenues !  
- Signalez les bugs ou proposez des améliorations via des issues GitHub.  
- Soumettez des pull requests pour améliorer le projet.

---

## Licence 📄

Ce projet est sous licence **MIT**. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

Merci d'utiliser **SpawnManager** ! Profitez d'une gestion simplifiée et efficace des points de spawn sur votre serveur Minecraft. 🎮✨
