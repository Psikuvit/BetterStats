package me.psikuvit.betterStats.api;

import me.psikuvit.betterStats.reward.Rarity;
import me.psikuvit.betterStats.utils.Stat;

public interface ItemStats {

    double getValue(Stat stat);
    void setValue(Stat stat, double value);
    void setRarity(Rarity rarity);
}
