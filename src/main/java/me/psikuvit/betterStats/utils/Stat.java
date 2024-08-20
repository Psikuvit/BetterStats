package me.psikuvit.betterStats.utils;

import me.psikuvit.betterStats.BetterStats;
import org.bukkit.NamespacedKey;

public enum Stat {
    MAX_HEALTH("max_hp"),
    HEALTH("hp"),
    STRENGTH("strength"),
    INTELLECT("intellect"),
    MANA("mana"),
    DEFENSE("defense"),
    CRITICAL("critical"),
    HASTE("haste"),
    SPIRIT("spirit");

    private final NamespacedKey key;

    Stat(String key) {
        this.key = new NamespacedKey(BetterStats.getPlugin(BetterStats.class), key);
    }

    public NamespacedKey getKey() {
        return key;
    }
}
