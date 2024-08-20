package me.psikuvit.betterStats.utils;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class PlayerStats {

    /**
     * Initializes the player's stats by setting default values for each stat.
     * This method should be called when a player first joins or resets their stats.
     *
     * @param player The player whose stats are being initialized.
     */
    public static void initialiseStats(Player player) {
        player.getPersistentDataContainer().set(Stat.MAX_HEALTH.getKey(), PersistentDataType.DOUBLE, 20D);
        player.getPersistentDataContainer().set(Stat.HEALTH.getKey(), PersistentDataType.DOUBLE, 20D);
        player.getPersistentDataContainer().set(Stat.STRENGTH.getKey(), PersistentDataType.DOUBLE, 10D);
        player.getPersistentDataContainer().set(Stat.INTELLECT.getKey(), PersistentDataType.DOUBLE, 100D);
        player.getPersistentDataContainer().set(Stat.MANA.getKey(), PersistentDataType.DOUBLE, 100D);
        player.getPersistentDataContainer().set(Stat.DEFENSE.getKey(), PersistentDataType.DOUBLE, 20D);
        player.getPersistentDataContainer().set(Stat.CRITICAL.getKey(), PersistentDataType.DOUBLE, 20D);
        player.getPersistentDataContainer().set(Stat.HASTE.getKey(), PersistentDataType.DOUBLE, 20D);
        player.getPersistentDataContainer().set(Stat.SPIRIT.getKey(), PersistentDataType.DOUBLE, 20D);
    }

    /**
     * Retrieves the value of a specific stat for the player.
     *
     * @param player The player whose stat value is being retrieved.
     * @param stat The stat to retrieve the value for.
     * @return The value of the specified stat, or 0 if the stat is not set.
     */
    public static double getValue(Player player, Stat stat) {
        return player.getPersistentDataContainer().getOrDefault(stat.getKey(), PersistentDataType.DOUBLE, 0D);
    }

    /**
     * Sets the value of a specific stat for the player.
     *
     * @param player The player whose stat value is being set.
     * @param stat The stat to set the value for.
     * @param value The value to set for the specified stat.
     */
    public static void setValue(Player player, Stat stat, double value) {
        player.getPersistentDataContainer().set(stat.getKey(), PersistentDataType.DOUBLE, value);
    }

    /**
     * Sets the player's current health.
     *
     * @param player The player whose health is being set.
     * @param value The health value to set for the player.
     */
    public static void setHealth(Player player, double value) {
        player.getPersistentDataContainer().set(Stat.HEALTH.getKey(), PersistentDataType.DOUBLE, value);
    }

    /**
     * Retrieves the player's maximum health value.
     *
     * @param player The player whose maximum health is being retrieved.
     * @return The maximum health value of the player, or null if not set.
     */
    public static Double getPlayerMaxHealth(Player player) {
        return player.getPersistentDataContainer().get(Stat.MAX_HEALTH.getKey(), PersistentDataType.DOUBLE);
    }

    /**
     * Retrieves the player's current health value.
     *
     * @param player The player whose current health is being retrieved.
     * @return The current health value of the player, or null if not set.
     */
    public static Double getPlayerHealth(Player player) {
        return player.getPersistentDataContainer().get(Stat.HEALTH.getKey(), PersistentDataType.DOUBLE);
    }

    /**
     * Calculates the damage reduction based on the player's defense stat.
     *
     * @param player The player whose defense is being applied to the damage.
     * @param incomeDamage The initial incoming damage before defense is applied.
     * @return The reduced damage after applying the player's defense stat.
     */
    public static double getDefenseReduction(Player player, double incomeDamage) {
        double defense = PlayerStats.getValue(player, Stat.DEFENSE);

        double reducedDamage = incomeDamage - (defense * 0.2);

        if (reducedDamage < 0) {
            reducedDamage = 0;
        }
        return reducedDamage;
    }

    /**
     * Calculates the damage dealt based on the player's intellect and mana stats.
     *
     * @param player The player whose intellect and mana are being used to calculate damage.
     * @return The calculated damage based on the player's intellect and mana.
     */
    public static double getManaDamage(Player player) {
        double currentMana = getValue(player, Stat.INTELLECT);
        return 1 + (currentMana / 50);
    }
}
