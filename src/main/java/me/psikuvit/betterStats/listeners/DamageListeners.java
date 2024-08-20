package me.psikuvit.betterStats.listeners;

import me.psikuvit.betterStats.Utils;
import me.psikuvit.betterStats.utils.PlayerStats;
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

            double strength = PlayerStats.getValue(player, Stat.STRENGTH);
            double baseDamage = event.getDamage() + strength * 0.5; // strength formula

            if (isCriticalHit(player)) { // check if the hit is critical
                double critMultiplier = 1.5;
                baseDamage *= critMultiplier; // add 150% more damage to the original amount
                player.sendMessage("Critical Hit! Damage increased to " + baseDamage);
            }

            event.setDamage(baseDamage); // applies the damage
            Utils.setDamageIndicator(event.getEntity().getLocation(), baseDamage);
        }
    }

    private boolean isCriticalHit(Player player) {
        return player.getFallDistance() > 0.0F // check if player is falling
                && player.getVelocity().getY() < 0
                && !player.isSprinting()
                && !player.isInWater();
    }
}
