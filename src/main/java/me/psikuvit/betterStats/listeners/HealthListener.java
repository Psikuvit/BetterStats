package me.psikuvit.betterStats.listeners;

import me.psikuvit.betterStats.BetterStats;
import me.psikuvit.betterStats.armor.ArmorEquipEvent;
import me.psikuvit.betterStats.stats.ItemStats;
import me.psikuvit.betterStats.stats.PlayerStats;
import me.psikuvit.betterStats.utils.Utils;
import me.psikuvit.betterStats.utils.Stat;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class HealthListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerStats playerStats = new PlayerStats(player);

            double reducedDamage = playerStats.getDefenseReduction(event.getDamage());
            double healthAfterDamage = (playerStats.getValue(Stat.HEALTH) + playerStats.getArmorStats(Stat.HEALTH)) - reducedDamage; // amount of remaining Health after defense application

            healthAfterDamage = Math.max(0, healthAfterDamage); // making sure that the amount is greater or equal to 0

            if (healthAfterDamage == 0) {
                player.setHealth(0); // handles player death
                return;
            }
            setHealth(healthAfterDamage, player);
            Utils.setDamageIndicator(player.getLocation(), reducedDamage);

            event.setDamage(0.00001);
        } else {
            Utils.setDamageIndicator(event.getEntity().getLocation(), event.getDamage());
        }
    }

    @EventHandler
    public void onPlayerRegainHealth(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerStats playerStats = new PlayerStats(player);
            event.setCancelled(true);

            double newHealth = playerStats.getValue(Stat.HEALTH) + event.getAmount(); // new regenerated health
            setHealth(newHealth, player);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        PlayerStats playerStats = new PlayerStats(player);
        setHealth(playerStats.getValue(Stat.MAX_HEALTH), player); // setting health to max on respawn
    }

    @EventHandler
    public void onEquip(ArmorEquipEvent event) {
        Bukkit.getScheduler().runTask(BetterStats.getPlugin(BetterStats.class), () ->
                setHealth(new PlayerStats(event.getPlayer()).getValue(Stat.HEALTH), event.getPlayer()));
    }


    public void setHealth(double health, Player player) {
        PlayerStats playerStats = new PlayerStats(player);

        double maxHealth = playerStats.getValue(Stat.MAX_HEALTH) + playerStats.getArmorStats(Stat.MAX_HEALTH);
        double currentHealth = playerStats.getValue(Stat.HEALTH);
        double newHealth = Math.max(0, Math.min(health, maxHealth)); // keeping health greater than 0 and smaller than max

        playerStats.setValue(Stat.HEALTH, newHealth); // saving the health
        double scale = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * (newHealth / maxHealth); // scaling health to hearts

        player.setHealth(Math.max(0.5, scale));

        if (newHealth < currentHealth) { // damage the player
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 1.0f);
        }
    }
}
