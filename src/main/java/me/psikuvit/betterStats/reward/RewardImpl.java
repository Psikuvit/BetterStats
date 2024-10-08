package me.psikuvit.betterStats.reward;

import com.google.common.math.Stats;
import me.psikuvit.betterStats.api.ItemStats;
import me.psikuvit.betterStats.api.Reward;
import me.psikuvit.betterStats.api.StatsAPI;
import me.psikuvit.betterStats.stats.ItemStatsImpl;
import me.psikuvit.betterStats.utils.ConfigUtils;
import me.psikuvit.betterStats.utils.Stat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class RewardImpl implements Reward {

    private static final Random rnd = new Random();

    public ItemStack getItemStack(int level) {
        Rarity rarity = Rarity.values()[rnd.nextInt(Rarity.values().length)];

        List<Material> mats = classifyArmorByRarity(rarity);
        Material material = mats.get(rnd.nextInt(mats.size()));

        ItemStack itemStack = new ItemStack(material);
        ItemStats itemStats = StatsAPI.getItemStats(itemStack);

        Set<Stat> appliedStats = new HashSet<>(); // To store applied stats

        for (int i = 0; rarity.getStats() >= i; i++) {
            Stat rndStat = randomStat(appliedStats); // Ensure no duplicates
            double rndAmount = randomStatAmount(rarity, level);

            itemStats.setValue(rndStat, rndAmount);
            appliedStats.add(rndStat); // Add stat to the set after applying it
        }
        itemStats.setRarity(rarity);
        return itemStack;
    }

    private Stat randomStat(Set<Stat> appliedStats) {
        Stat stat;
        do {
            stat = Stat.values()[rnd.nextInt(Stat.values().length)];
        } while (stat == Stat.CURRENT_HP || appliedStats.contains(stat)); // Avoid duplicates and CURRENT_HP
        return stat;
    }

    private double randomStatAmount(Rarity rarity, int level) {
        int min = 0;
        int max = 0;
        if (rarity == Rarity.RARE) {
            min = ConfigUtils.getRareLevel(level)[0];
            max = ConfigUtils.getRareLevel(level)[1];
        } else if (rarity == Rarity.EPIC) {
            min = ConfigUtils.getEpicLevel(level)[0];
            max = ConfigUtils.getEpicLevel(level)[1];
        } else if (rarity == Rarity.EpicWarforged) {
            min = ConfigUtils.getEpicWarforgedLevel(level)[0];
            max = ConfigUtils.getEpicWarforgedLevel(level)[1];
        } else if (rarity == Rarity.EpicTitanforged) {
            min = ConfigUtils.getEpicTitanforgedLevel(level)[0];
            max = ConfigUtils.getEpicTitanforgedLevel(level)[1];
        }

        return rnd.nextInt(max - min + 1) + min;
    }

    private List<Material> classifyArmorByRarity(Rarity rarity) {
        List<Material> armorList = new ArrayList<>();

        switch (rarity) {
            case RARE:
                armorList.add(Material.IRON_HELMET);
                armorList.add(Material.IRON_CHESTPLATE);
                armorList.add(Material.IRON_LEGGINGS);
                armorList.add(Material.IRON_BOOTS);
                break;

            case EPIC:
                armorList.add(Material.DIAMOND_HELMET);
                armorList.add(Material.DIAMOND_CHESTPLATE);
                armorList.add(Material.DIAMOND_LEGGINGS);
                armorList.add(Material.DIAMOND_BOOTS);
                break;

            case EpicWarforged, EpicTitanforged:
                armorList.add(Material.NETHERITE_HELMET);
                armorList.add(Material.NETHERITE_CHESTPLATE);
                armorList.add(Material.NETHERITE_LEGGINGS);
                armorList.add(Material.NETHERITE_BOOTS);
                break;

            default:
                break;
        }

        return armorList;
    }
}
