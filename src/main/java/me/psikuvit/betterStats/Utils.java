package me.psikuvit.betterStats;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Utils {

    public static void setDamageIndicator(Location loc, double damage) {
        double randomX = Math.random();
        double randomY = Math.random();
        double randomZ = Math.random();
        randomX -= 0.5D;
        randomY += 0.25D;
        randomZ -= 0.5D;
        ArmorStand armorStand = loc.getWorld().spawn(loc.clone().add(randomX, randomY, randomZ), ArmorStand.class);
        armorStand.setCustomName(color((int) damage));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setInvisible(true);
        armorStand.setBasePlate(false);
        armorStand.setGravity(false);

        Bukkit.getScheduler().runTaskLater(BetterStats.getPlugin(BetterStats.class), armorStand::remove, 20);
    }

    public static String color(int text) {
        List<ChatColor> CRIT_SPECTRUM = List.of(ChatColor.WHITE, ChatColor.WHITE, ChatColor.YELLOW, ChatColor.GOLD, ChatColor.RED, ChatColor.RED);
        String s = "<" + text + ">";

        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String c : s.split("")) {
            if (i > CRIT_SPECTRUM.size() - 1)
                i = 0;
            builder.append(CRIT_SPECTRUM.get(i)).append(c);
            i++;
        }
        return builder.toString();
    }
}
