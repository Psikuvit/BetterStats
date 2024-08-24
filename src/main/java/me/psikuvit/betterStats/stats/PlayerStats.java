package me.psikuvit.betterStats.stats;

import me.psikuvit.betterStats.utils.Stat;
import me.psikuvit.betterStats.utils.Utils;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public record PlayerStats(Player player) {

    /**
     * Initializes the player's stats by setting default values for each stat.
     * This method should be called when a player first joins or resets their stats.
     */
    public void initialiseStats() {
        player.getPersistentDataContainer().set(Stat.MAX_HEALTH.getKey(), PersistentDataType.DOUBLE, 20D);
        player.getPersistentDataContainer().set(Stat.HEALTH.getKey(), PersistentDataType.DOUBLE, 20D);
        player.getPersistentDataContainer().set(Stat.STRENGTH.getKey(), PersistentDataType.DOUBLE, 10D);
        player.getPersistentDataContainer().set(Stat.INTELLECT.getKey(), PersistentDataType.DOUBLE, 100D);
        player.getPersistentDataContainer().set(Stat.MANA.getKey(), PersistentDataType.DOUBLE, 100D);
        player.getPersistentDataContainer().set(Stat.DEFENSE.getKey(), PersistentDataType.DOUBLE, 20D);
        player.getPersistentDataContainer().set(Stat.CRITICAL.getKey(), PersistentDataType.DOUBLE, 20D);
        player.getPersistentDataContainer().set(Stat.HASTE.getKey(), PersistentDataType.DOUBLE, 4D);
        player.getPersistentDataContainer().set(Stat.SPIRIT.getKey(), PersistentDataType.DOUBLE, 20D);
    }

    /**
     * Retrieves the value of a specific stat for the player.
     *
     * @param stat The stat to retrieve the value for.
     * @return The value of the specified stat, or 0 if the stat is not set.
     */
    public double getValue(Stat stat) {
        return player.getPersistentDataContainer().getOrDefault(stat.getKey(), PersistentDataType.DOUBLE, 0D);
    }

    /**
     * Sets the value of a specific stat for the player.
     *
     * @param stat The stat to set the value for.
     * @param value The value to set for the specified stat.
     */
    public void setValue(Stat stat, double value) {
        if (stat == Stat.HASTE) {
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(value);
        }
        player.getPersistentDataContainer().set(stat.getKey(), PersistentDataType.DOUBLE, value);

    }

    /**
     * Calculates the damage reduction based on the player's defense stat.
     *
     * @param incomeDamage The initial incoming damage before defense is applied.
     * @return The reduced damage after applying the player's defense stat.
     */
    public double getDefenseReduction(double incomeDamage) {
        double defense = getValue(Stat.DEFENSE);

        double reducedDamage = incomeDamage - (defense * 0.2);

        if (reducedDamage < 0) {
            reducedDamage = 0;
        }
        return reducedDamage;
    }

    /**
     * Calculates the total value of a specific stat from the player's equipped armor.
     *
     * <p>This method iterates through all the items in the player's armor slots
     * (helmet, chestplate, leggings, and boots) and sums up the value of the specified stat.
     * The stat value is retrieved from the item's PersistentDataContainer using the stat's key.
     * If an armor piece is null or does not contain the specified stat in its PersistentDataContainer,
     * it contributes a value of 0 to the sum.</p>
     *
     * @param stat The {@link Stat} representing the specific stat to calculate the total for.
     * @return The total value of the specified stat across all the player's equipped armor.
     */
    public double getArmorStats(Stat stat) {
        double sum = 0;
        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
            if (itemStack == null || itemStack.getItemMeta() == null) continue;
            ItemStats itemStat = new ItemStats(itemStack);

            sum += itemStat.getValue(stat);
        }
        return sum;
    }

    public double getHeldStats(Stat stat) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getItemMeta() == null) return 0;

        ItemStats itemStats = new ItemStats(itemStack);
        return itemStats.getValue(stat);
    }

    /**
     * Calculates the damage dealt based on the player's intellect and mana stats.
     *
     * @return The calculated damage based on the player's intellect and mana.
     */
    public double getManaDamage() {
        double currentMana = getValue(Stat.INTELLECT);
        return 1 + (currentMana / 50);
    }

    /**
     * Applies magic damage to a specified entity and deducts mana cost from the caster.
     * <p>
     * This method calculates the amount of damage based on the caster's intellect and mana stats.
     * It then applies this damage to the target entity, provided that the caster has enough mana
     * to cover the specified mana cost. The mana cost is deducted from the caster's mana after
     * applying the damage.
     * </p>
     * @param castedOn The entity that will receive the damage. The entity should be an instance
     *                 of {@link LivingEntity} to apply damage. If it is not,
     *                 the damage will not be applied.
     * @param manaCost The amount of mana required to cast the spell. This value will be deducted
     *                 from the caster’s current mana. If the caster does not have enough mana,
     *                 the spell will not be cast, and no damage will be applied.
     */
    public void applyMagicDamage(Entity castedOn, double manaCost) {
        double currentMana = getValue(Stat.MANA);
        if (currentMana < manaCost) {
            player.sendMessage(Utils.color("&cNot enough mana!"));
            return;
        }

        double damage = getManaDamage();

        setValue(Stat.MANA, currentMana - manaCost);

        if (castedOn instanceof LivingEntity target) {
            target.damage(damage);
        }
    }
}