package me.psikuvit.betterStats;

import me.psikuvit.betterStats.listeners.DamageListeners;
import me.psikuvit.betterStats.listeners.HealthListener;
import me.psikuvit.betterStats.utils.PlayerStats;
import me.psikuvit.betterStats.utils.Stat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

        getCommand("bs").setExecutor(this);

        getServer().getPluginManager().registerEvents(new DamageListeners(), this);
        getServer().getPluginManager().registerEvents(new HealthListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) return true;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("test")) {
                PlayerStats.initialiseStats(player);
            } else if (args[0].equalsIgnoreCase("show")) {
                player.sendMessage("INTELLECT: " + PlayerStats.getValue(player, Stat.INTELLECT));
                player.sendMessage("MANA: " + PlayerStats.getValue(player, Stat.MANA));
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set")) {
                Stat stat;
                int amount;
                try {
                    stat = Stat.valueOf(args[1]);
                    amount = Integer.parseInt(args[2]);
                } catch (IllegalArgumentException ex) {
                    player.sendMessage("Wrong stat");
                    return true;
                }
                if (stat == Stat.HEALTH) {
                    PlayerStats.setHealth(player, amount);
                    return true;
                }

                PlayerStats.setValue(player, stat, amount);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Stream.of("test", "show", "set")
                    .filter(subcommand -> subcommand.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            return Arrays.stream(Stat.values())
                    .map(Stat::name)
                    .filter(stat -> stat.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());

        }
        return Collections.emptyList();
    }
}
