package me.psikuvit.betterStats.stats;

import me.psikuvit.betterStats.api.ItemStats;
import me.psikuvit.betterStats.reward.Rarity;
import me.psikuvit.betterStats.utils.Stat;
import me.psikuvit.betterStats.utils.Utils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record ItemStatsImpl(ItemStack itemStack) implements ItemStats {

    public ItemStatsImpl {
        if (itemStack == null || itemStack.getItemMeta() == null) {
            throw new IllegalArgumentException("ItemStack or ItemMeta cannot be null.");
        }
    }

    @Override
    public double getValue(Stat stat) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
        return pdc.getOrDefault(stat.getKey(), PersistentDataType.DOUBLE, 0D);
    }

    @Override
    public void setValue(Stat stat, double value) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();

        List<String> lore = Optional.ofNullable(itemMeta.getLore()).orElseGet(ArrayList::new);
        lore.add(Utils.color("&7" + stat.getKey().getKey() + " &b" + ((int) value)));

        itemMeta.setLore(lore);
        pdc.set(stat.getKey(), PersistentDataType.DOUBLE, value);
        itemStack.setItemMeta(itemMeta);
    }

    @Override
    public void setRarity(Rarity rarity) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = Optional.ofNullable(itemMeta.getLore()).orElseGet(ArrayList::new);
        lore.add(" ");
        lore.add(rarity.getName());

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }
}
