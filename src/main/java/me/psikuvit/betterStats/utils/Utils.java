package me.psikuvit.betterStats.utils;

import me.psikuvit.betterStats.BetterStats;
import me.psikuvit.betterStats.stats.ItemStats;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Utils {

    private Utils() {}

    public static void setDamageIndicator(Location loc, double damage) {
        double randomX = Math.random();
        double randomY = Math.random();
        double randomZ = Math.random();
        randomX -= 0.5D;
        randomY += 0.25D;
        randomZ -= 0.5D;
        ArmorStand armorStand = loc.getWorld().spawn(loc.clone().add(randomX, randomY, randomZ), ArmorStand.class, as -> {
        as.setCustomName(color((int) damage));
        as.setCustomNameVisible(true);
        as.setInvulnerable(true);
        as.setInvisible(true);
        as.setBasePlate(false);
        as.setGravity(false);
    });

        Bukkit.getScheduler().runTaskLater(BetterStats.getPlugin(BetterStats.class), armorStand::remove, 20);
    }

    private static String color(int damage) {
        List<ChatColor> CRIT_SPECTRUM = List.of(ChatColor.WHITE, ChatColor.WHITE, ChatColor.YELLOW, ChatColor.GOLD, ChatColor.RED, ChatColor.RED);
        String s = "<" + damage + ">";

        StringBuilder builder = new StringBuilder();
        int spectrumSize = CRIT_SPECTRUM.size();

        for (int i = 0; i < s.length(); i++) {
            builder.append(CRIT_SPECTRUM.get(i % spectrumSize)).append(s.charAt(i));
        }

        return builder.toString();
    }

    public static String color(String msg) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', msg);
    }
}
