package me.psikuvit.betterStats.stats;

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

public class ItemStats {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;
    private final PersistentDataContainer pdc;

    public ItemStats(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        this.pdc = itemMeta.getPersistentDataContainer();
    }

    public double getValue(Stat stat) {
        return pdc.getOrDefault(stat.getKey(), PersistentDataType.DOUBLE, 0D);
    }

    public void setValue(Stat stat, double value) {
        List<String> lore = Optional.ofNullable(itemMeta.getLore()).orElseGet(ArrayList::new);
        lore.add(Utils.color("&7" + stat.getKey().getKey() + " &b" + ((int) value)));

        itemMeta.setLore(lore);
        pdc.set(stat.getKey(), PersistentDataType.DOUBLE, value);
        itemStack.setItemMeta(itemMeta);
    }

    public void setRarity(Rarity rarity) {
        List<String> lore = Optional.ofNullable(itemMeta.getLore()).orElseGet(ArrayList::new);
        lore.add(" ");
        lore.add(rarity.getName());

        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
    }

}
