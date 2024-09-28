package me.psikuvit.betterStats.utils;

import me.psikuvit.betterStats.BetterStats;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtils {

    private static FileConfiguration config;

    public static void init(BetterStats plugin) {
        ConfigUtils.config = plugin.getConfig();
    }

    public static int[] getRareLevel(int level) {
        String string = config.getString("level-" + level + ".Rare");
        String[] s = string.split("-");
        int min = Integer.parseInt(s[0]);
        int max = Integer.parseInt(s[1]);
        return new int[] {min, max};
    }

    public static int[] getEpicLevel(int level) {
        String string = config.getString("level-" + level + ".Epic");
        String[] s = string.split("-");
        int min = Integer.parseInt(s[0]);
        int max = Integer.parseInt(s[1]);
        return new int[] {min, max};
    }

    public static int[] getEpicWarforgedLevel(int level) {
        String string = config.getString("level-" + level + ".EpicWarforged");
        String[] s = string.split("-");
        int min = Integer.parseInt(s[0]);
        int max = Integer.parseInt(s[1]);
        return new int[] {min, max};
    }

    public static int[] getEpicTitanforgedLevel(int level) {
        String string = config.getString("level-" + level + ".EpicTitanforged");
        String[] s = string.split("-");
        int min = Integer.parseInt(s[0]);
        int max = Integer.parseInt(s[1]);
        return new int[] {min, max};
    }

}
