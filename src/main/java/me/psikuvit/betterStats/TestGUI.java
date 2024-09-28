package me.psikuvit.betterStats;

import me.psikuvit.betterStats.stats.PlayerStatsImpl;
import me.psikuvit.betterStats.utils.Stat;
import me.psikuvit.betterStats.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TestGUI implements InventoryHolder {

    private final Inventory inventory;
    private final PlayerStatsImpl playerStats;

    public TestGUI(Player player) {
        this.inventory = Bukkit.createInventory(this, 36, "test");
        this.playerStats = new PlayerStatsImpl(player);

        for (int i = 0; inventory.getSize() > i; i++) inventory.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(Utils.color("&7" + player.getName() + "'s stats"));
        List<String> lore = new ArrayList<>();
        for (Stat stat : Stat.values()) {
            double sum = playerStats.getValue(stat) + playerStats.getArmorStats(stat) + playerStats.getHeldStats(stat);
            lore.add(Utils.color("&7" + stat.getKey().getKey() + " &b" + sum));
        }
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        inventory.setItem(13, itemStack);
    }

    public void openInv() {
        playerStats.player().openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
