package me.psikuvit.betterStats;

import me.psikuvit.betterStats.armor.ArmorListener;
import me.psikuvit.betterStats.listeners.DamageListeners;
import me.psikuvit.betterStats.listeners.HealthListener;
import me.psikuvit.betterStats.listeners.JoinListener;
import me.psikuvit.betterStats.reward.ConfigUtils;
import me.psikuvit.betterStats.reward.Reward;
import me.psikuvit.betterStats.stats.ItemStats;
import me.psikuvit.betterStats.stats.PlayerStats;
import me.psikuvit.betterStats.utils.Stat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class BetterStats extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        ConfigUtils.init(this);

        getCommand("bs").setExecutor(this);

        getServer().getPluginManager().registerEvents(new DamageListeners(), this);
        getServer().getPluginManager().registerEvents(new HealthListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;
        PlayerStats playerStats = new PlayerStats(player);

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("test")) {
                playerStats.initialiseStats();
            } else if (args[0].equalsIgnoreCase("show")) {
                new TestGUI(player).openInv();
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("reward")) {
                int level = Integer.parseInt(args[1]);
                player.getInventory().addItem(new Reward(level).getItemStack());
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                Stat stat;
                double amount;
                try {
                    stat = Stat.valueOf(args[1]);
                    amount = Double.parseDouble(args[2]);
                } catch (IllegalArgumentException ex) {
                    player.sendMessage("Wrong stat");
                    return true;
                }

                playerStats.setValue(stat, amount);
            } else if (args[0].equalsIgnoreCase("item")) {
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack.getType().isAir()) return true;
                Stat stat;
                double amount;
                try {
                    stat = Stat.valueOf(args[1]);
                    amount = Double.parseDouble(args[2]);
                } catch (IllegalArgumentException ex) {
                    player.sendMessage("Wrong stat");
                    return true;
                }

                new ItemStats(itemStack).setValue(stat, amount);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Stream.of("test", "reward" ,"show", "set", "item")
                    .filter(subcommand -> subcommand.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("item"))) {
            return Arrays.stream(Stat.values())
                    .map(Stat::name)
                    .filter(stat -> stat.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());

        }
        return Collections.emptyList();
    }
}
