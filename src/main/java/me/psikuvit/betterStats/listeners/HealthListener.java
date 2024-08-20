package me.psikuvit.betterStats.listeners;

import me.psikuvit.betterStats.Utils;
import me.psikuvit.betterStats.utils.PlayerStats;
import me.psikuvit.betterStats.utils.Stat;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class HealthListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            event.setCancelled(true);

            double reducedDamage = PlayerStats.getDefenseReduction(player, event.getDamage());
            double healthAfterDamage = PlayerStats.getPlayerHealth(player) - reducedDamage; // amount of remaining Health after defense application

            healthAfterDamage = Math.max(0, healthAfterDamage); // making sure that the amount is greater or equal to 0

            setHealth(healthAfterDamage, player);
            Utils.setDamageIndicator(player.getLocation(), reducedDamage);

            if (healthAfterDamage <= 0) player.setHealth(0); // handles player death
        }
    }

    @EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player player) {
            event.setCancelled(true);

            double newHealth = PlayerStats.getPlayerHealth(player) + event.getAmount(); // new regenerated health
            setHealth(newHealth, player);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        setHealth(PlayerStats.getPlayerMaxHealth(player), player); // setting health to max on respawn
    }


    public void setHealth(double health, Player player) {
        double maxHealth = PlayerStats.getPlayerMaxHealth(player);
        double currentHealth = PlayerStats.getPlayerHealth(player);

        double newHealth = Math.max(0, Math.min(health, maxHealth)); // keeping health greater than 0 and smaller than max
        if (newHealth == currentHealth) return;

        PlayerStats.setHealth(player, newHealth); // saving the health
        double scale = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * (newHealth / maxHealth); // scaling health to hearts

        player.setHealth(Math.max(0.5, scale));

        if (newHealth < currentHealth) { // damage the player
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
            player.setNoDamageTicks(0);
            player.damage(0);
        }
    }
}
