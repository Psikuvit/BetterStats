package me.psikuvit.betterStats.reward;

public enum Rarity {
    RARE(2),
    EPIC(2),
    EpicWarforged(3),
    EpicTitanforged(3);

    private final int stats;

    Rarity(int stats) {
        this.stats = stats;
    }

    public int getStats() {
        return stats;
    }
}
