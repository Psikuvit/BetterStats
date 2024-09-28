package me.psikuvit.betterStats.api;

import me.psikuvit.betterStats.utils.Stat;
import org.bukkit.entity.Entity;

public interface PlayerStats {

    void initialiseStats();

    double getValue(Stat stat);

    void setValue(Stat stat, double value);

    double getDefenseReduction(double incomeDamage);

    double getArmorStats(Stat stat);

    double getHeldStats(Stat stat);

    double getManaDamage();

    void applyMagicDamage(Entity castedOn, double manaCost);
}
