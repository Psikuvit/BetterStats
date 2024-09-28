package me.psikuvit.betterStats.utils;

public class Utils {

    private Utils() {}

    public static String color(String msg) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', msg);
    }
}
