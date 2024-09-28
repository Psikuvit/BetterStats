package me.psikuvit.betterStats.api;

import me.psikuvit.betterStats.reward.RewardImpl;
import me.psikuvit.betterStats.stats.ItemStatsImpl;
import me.psikuvit.betterStats.stats.PlayerStatsImpl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StatsAPI {

    public static ItemStats getItemStats(ItemStack itemStack) {
        return new ItemStatsImpl(itemStack);
    }

    public static PlayerStats getPlayerStats(Player player) {
        return new PlayerStatsImpl(player);
    }

    public static Reward getReward() {
        return new RewardImpl();
    }
}
