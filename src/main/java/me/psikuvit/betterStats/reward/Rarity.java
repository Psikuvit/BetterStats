package me.psikuvit.betterStats.reward;

import me.psikuvit.betterStats.utils.Utils;

public enum Rarity {
    RARE(2, "&1Rare"),
    EPIC(2, "&9Epic"),
    EpicWarforged(3, "&6Epic War Forged"),
    EpicTitanforged(3, "&9Epic Titan Forged");

    private final int stats;
    private final String name;

    Rarity(int stats, String name) {
        this.stats = stats;
        this.name = Utils.color(name);
    }

    public int getStats() {
        return stats;
    }

    public String getName() {
        return name;
    }
}
