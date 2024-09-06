package me.psikuvit.betterStats.listeners;

import me.psikuvit.betterStats.TestGUI;
import me.psikuvit.betterStats.stats.PlayerStats;
import me.psikuvit.betterStats.utils.Stat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.getPersistentDataContainer().has(Stat.CURRENT_HP.getKey())) return;
        PlayerStats playerStats = new PlayerStats(player);
        playerStats.initialiseStats();

    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory().getHolder() instanceof TestGUI) {
            event.setCancelled(true);
        }
    }
}
