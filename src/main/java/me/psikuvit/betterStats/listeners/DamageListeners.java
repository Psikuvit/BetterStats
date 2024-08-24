package me.psikuvit.betterStats.listeners;

import me.psikuvit.betterStats.stats.ItemStats;
import me.psikuvit.betterStats.stats.PlayerStats;
import me.psikuvit.betterStats.utils.Stat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListeners implements Listener {


    // This handles the player damage stats (strength, crit)
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            PlayerStats playerStats = new PlayerStats(player);

            double playerStrength = playerStats.getValue(Stat.STRENGTH) + playerStats.getArmorStats(Stat.STRENGTH);
            double itemStrength = playerStats.getHeldStats(Stat.STRENGTH);

            double baseDamage = itemStrength + playerStrength * 0.5; // strength formula

            if (isCriticalHit(player)) { // check if the hit is critical
                double critMultiplier = 1.5 + (playerStats.getHeldStats(Stat.CRITICAL) + playerStats.getArmorStats(Stat.CRITICAL) * 0.2) / 100;
                baseDamage *= critMultiplier;
                player.sendMessage("Critical Hit! Damage increased to " + baseDamage);
            }

            event.setDamage(baseDamage); // applies the damage
        }
    }

    private boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0.0F // check if player is falling
                && player.getVelocity().getY() < 0
                && !player.isSprinting()
                && !player.isInWater();
    }
}
