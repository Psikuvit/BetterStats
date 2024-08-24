package me.psikuvit.betterStats.reward;

import me.psikuvit.betterStats.stats.ItemStats;
import me.psikuvit.betterStats.utils.Stat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public class Reward {

    private final ItemStack itemStack;

    public Reward(int level) {
        Material material = getArmorMaterials().get(new Random().nextInt(getArmorMaterials().size()));
        itemStack = new ItemStack(material);
        ItemStats itemStats = new ItemStats(itemStack);

        Rarity rarity = Rarity.values()[new Random().nextInt(Rarity.values().length)];

        for (int i = 0; rarity.getStats() >= i; i++) {
            itemStats.setValue(getSelectedStat(), randomStatAmount(rarity, level));
        }
        itemStats.setRarity(rarity);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Stat getSelectedStat() {
        return Stat.values()[(new Random().nextInt(Stat.values().length))];
    }

    public double randomStatAmount(Rarity rarity, int level) {
        Random random = new Random();
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

        return random.nextInt((int) (max - min + 1)) + min;
    }

    public List<Material> getArmorMaterials() {
        return List.of(
                Material.LEATHER_HELMET,
                Material.LEATHER_CHESTPLATE,
                Material.LEATHER_LEGGINGS,
                Material.LEATHER_BOOTS,

                Material.CHAINMAIL_HELMET,
                Material.CHAINMAIL_CHESTPLATE,
                Material.CHAINMAIL_LEGGINGS,
                Material.CHAINMAIL_BOOTS,

                Material.IRON_HELMET,
                Material.IRON_CHESTPLATE,
                Material.IRON_LEGGINGS,
                Material.IRON_BOOTS,

                Material.GOLDEN_HELMET,
                Material.GOLDEN_CHESTPLATE,
                Material.GOLDEN_LEGGINGS,
                Material.GOLDEN_BOOTS,

                Material.DIAMOND_HELMET,
                Material.DIAMOND_CHESTPLATE,
                Material.DIAMOND_LEGGINGS,
                Material.DIAMOND_BOOTS,

                Material.NETHERITE_HELMET,
                Material.NETHERITE_CHESTPLATE,
                Material.NETHERITE_LEGGINGS,
                Material.NETHERITE_BOOTS,

                Material.TURTLE_HELMET
        );
    }
}
