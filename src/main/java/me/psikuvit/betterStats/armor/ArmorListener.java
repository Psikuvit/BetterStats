package me.psikuvit.betterStats.armor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.List;

public class ArmorListener implements Listener {

    private final List<String> blockedItems = List.of(
            "FURNACE",
            "CHEST",
            "TRAPPED_CHEST",
            "BEACON",
            "DISPENSER",
            "DROPPER",
            "HOPPER",
            "WORKBENCH",
            "ENCHANTMENT_TABLE",
            "ENDER_CHEST",
            "ANVIL",
            "BED_BLOCK",
            "FENCE_GATE",
            "SPRUCE_FENCE_GATE",
            "BIRCH_FENCE_GATE",
            "ACACIA_FENCE_GATE",
            "JUNGLE_FENCE_GATE",
            "DARK_OAK_FENCE_GATE",
            "IRON_DOOR_BLOCK",
            "WOODEN_DOOR",
            "SPRUCE_DOOR",
            "BIRCH_DOOR",
            "JUNGLE_DOOR",
            "ACACIA_DOOR",
            "DARK_OAK_DOOR",
            "WOOD_BUTTON",
            "STONE_BUTTON",
            "TRAP_DOOR",
            "IRON_TRAPDOOR",
            "DIODE_BLOCK_OFF",
            "DIODE_BLOCK_ON",
            "REDSTONE_COMPARATOR_OFF",
            "REDSTONE_COMPARATOR_ON",
            "FENCE",
            "SPRUCE_FENCE",
            "BIRCH_FENCE",
            "JUNGLE_FENCE",
            "DARK_OAK_FENCE",
            "ACACIA_FENCE",
            "NETHER_FENCE",
            "BREWING_STAND",
            "CAULDRON",
            "LEGACY_SIGN_POST",
            "LEGACY_WALL_SIGN",
            "LEGACY_SIGN",
            "ACACIA_SIGN",
            "ACACIA_WALL_SIGN",
            "BIRCH_SIGN",
            "BIRCH_WALL_SIGN",
            "DARK_OAK_SIGN",
            "DARK_OAK_WALL_SIGN",
            "JUNGLE_SIGN",
            "JUNGLE_WALL_SIGN",
            "OAK_SIGN",
            "OAK_WALL_SIGN",
            "SPRUCE_SIGN",
            "SPRUCE_WALL_SIGN",
            "LEVER",
            "BLACK_SHULKER_BOX",
            "BLUE_SHULKER_BOX",
            "BROWN_SHULKER_BOX",
            "CYAN_SHULKER_BOX",
            "GRAY_SHULKER_BOX",
            "GREEN_SHULKER_BOX",
            "LIGHT_BLUE_SHULKER_BOX",
            "LIME_SHULKER_BOX",
            "MAGENTA_SHULKER_BOX",
            "ORANGE_SHULKER_BOX",
            "PINK_SHULKER_BOX",
            "PURPLE_SHULKER_BOX",
            "RED_SHULKER_BOX",
            "SILVER_SHULKER_BOX",
            "WHITE_SHULKER_BOX",
            "YELLOW_SHULKER_BOX",
            "DAYLIGHT_DETECTOR_INVERTED",
            "DAYLIGHT_DETECTOR",
            "BARREL",
            "BLAST_FURNACE",
            "SMOKER",
            "CARTOGRAPHY_TABLE",
            "COMPOSTER",
            "GRINDSTONE",
            "LECTERN",
            "LOOM",
            "STONECUTTER",
            "BELL"
    );

    @EventHandler(priority =  EventPriority.HIGHEST, ignoreCancelled = true)
    public final void inventoryClick(final InventoryClickEvent e){
        boolean shift = false, numberkey = false;
        if(e.getAction() == InventoryAction.NOTHING) return;// Why does this get called if nothing happens??
        if(e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)){
            shift = true;
        }
        if(e.getClick().equals(ClickType.NUMBER_KEY)){
            numberkey = true;
        }
        if(e.getSlotType() != InventoryType.SlotType.ARMOR && e.getSlotType() != InventoryType.SlotType.QUICKBAR && e.getSlotType() != InventoryType.SlotType.CONTAINER) return;
        if(e.getClickedInventory() != null && !e.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;
        if (!e.getInventory().getType().equals(InventoryType.CRAFTING) && !e.getInventory().getType().equals(InventoryType.PLAYER)) return;
        if(!(e.getWhoClicked() instanceof Player)) return;
        ArmorType newArmorType = ArmorType.matchType(shift ? e.getCurrentItem() : e.getCursor());
        if(!shift && newArmorType != null && e.getRawSlot() != newArmorType.getSlot()){
            // Used for drag and drop checking to make sure you aren't trying to place a helmet in the boots slot.
            return;
        }
        if(shift){
            newArmorType = ArmorType.matchType(e.getCurrentItem());
            if(newArmorType != null){
                boolean equipping = e.getRawSlot() != newArmorType.getSlot();
                if(newArmorType.equals(ArmorType.HELMET) && (equipping == isAirOrNull(e.getWhoClicked().getInventory().getHelmet())) || newArmorType.equals(ArmorType.CHESTPLATE) && (equipping == isAirOrNull(e.getWhoClicked().getInventory().getChestplate())) || newArmorType.equals(ArmorType.LEGGINGS) && (equipping == isAirOrNull(e.getWhoClicked().getInventory().getLeggings())) || newArmorType.equals(ArmorType.BOOTS) && (equipping == isAirOrNull(e.getWhoClicked().getInventory().getBoots()))){
                    ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) e.getWhoClicked(), ArmorEquipEvent.EquipMethod.SHIFT_CLICK, newArmorType, equipping ? null : e.getCurrentItem(), equipping ? e.getCurrentItem() : null);
                    Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                    if(armorEquipEvent.isCancelled()){
                        e.setCancelled(true);
                    }
                }
            }
        }else{
            ItemStack newArmorPiece = e.getCursor();
            ItemStack oldArmorPiece = e.getCurrentItem();
            if(numberkey){
                if(e.getClickedInventory().getType().equals(InventoryType.PLAYER)){// Prevents shit in the 2by2 crafting
                    // e.getClickedInventory() == The players inventory
                    // e.getHotBarButton() == key people are pressing to equip or unequip the item to or from.
                    // e.getRawSlot() == The slot the item is going to.
                    // e.getSlot() == Armor slot, can't use e.getRawSlot() as that gives a hotbar slot ;-;
                    ItemStack hotbarItem = e.getClickedInventory().getItem(e.getHotbarButton());
                    if(!isAirOrNull(hotbarItem)){// Equipping
                        newArmorType = ArmorType.matchType(hotbarItem);
                        newArmorPiece = hotbarItem;
                        oldArmorPiece = e.getClickedInventory().getItem(e.getSlot());
                    }else{// Unequipping
                        newArmorType = ArmorType.matchType(!isAirOrNull(e.getCurrentItem()) ? e.getCurrentItem() : e.getCursor());
                    }
                }
            }else{
                if(isAirOrNull(e.getCursor()) && !isAirOrNull(e.getCurrentItem())){// unequip with no new item going into the slot.
                    newArmorType = ArmorType.matchType(e.getCurrentItem());
                }
                // e.getCurrentItem() == Unequip
                // e.getCursor() == Equip
                // newArmorType = ArmorType.matchType(!isAirOrNull(e.getCurrentItem()) ? e.getCurrentItem() : e.getCursor());
            }
            if(newArmorType != null && e.getRawSlot() == newArmorType.getSlot()){
                ArmorEquipEvent.EquipMethod method = ArmorEquipEvent.EquipMethod.PICK_DROP;
                if(e.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberkey) method = ArmorEquipEvent.EquipMethod.HOTBAR_SWAP;
                ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) e.getWhoClicked(), method, newArmorType, oldArmorPiece, newArmorPiece);
                Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                if(armorEquipEvent.isCancelled()){
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority =  EventPriority.HIGHEST)
    public void playerInteractEvent(PlayerInteractEvent e){
        if(e.useItemInHand().equals(Event.Result.DENY))return;
        //
        if(e.getAction() == Action.PHYSICAL) return;
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            Player player = e.getPlayer();
            if(!e.useInteractedBlock().equals(Event.Result.DENY)){
                if(e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && !player.isSneaking()){// Having both of these checks is useless, might as well do it though.
                    // Some blocks have actions when you right-click them which stops the client from equipping the armor in hand.
                    Material mat = e.getClickedBlock().getType();
                    for(String s : blockedItems){
                        if(mat.name().equalsIgnoreCase(s)) return;
                    }
                }
            }
            ArmorType newArmorType = ArmorType.matchType(e.getItem());
            if(newArmorType != null){
                if(newArmorType.equals(ArmorType.HELMET) && isAirOrNull(e.getPlayer().getInventory().getHelmet()) || newArmorType.equals(ArmorType.CHESTPLATE) && isAirOrNull(e.getPlayer().getInventory().getChestplate()) || newArmorType.equals(ArmorType.LEGGINGS) && isAirOrNull(e.getPlayer().getInventory().getLeggings()) || newArmorType.equals(ArmorType.BOOTS) && isAirOrNull(e.getPlayer().getInventory().getBoots())){
                    ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getPlayer(), ArmorEquipEvent.EquipMethod.HOTBAR, ArmorType.matchType(e.getItem()), null, e.getItem());
                    Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
                    if(armorEquipEvent.isCancelled()){
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority =  EventPriority.HIGHEST, ignoreCancelled = true)
    public void inventoryDrag(InventoryDragEvent event){
        // getType() seems to always be even.
        // Old Cursor gives the item you are equipping
        // Raw slot is the ArmorType slot
        // Can't replace armor using this method making getCursor() useless.
        ArmorType type = ArmorType.matchType(event.getOldCursor());
        if(event.getRawSlots().isEmpty()) return;// Idk if this will ever happen
        if(type != null && type.getSlot() == event.getRawSlots().stream().findFirst().orElse(0)){
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) event.getWhoClicked(), ArmorEquipEvent.EquipMethod.DRAG, type, null, event.getOldCursor());
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if(armorEquipEvent.isCancelled()){
                event.setResult(Event.Result.DENY);
                event.setCancelled(true);
            }
        }
        // Debug shit
    }

    @EventHandler
    public void itemBreakEvent(PlayerItemBreakEvent e){
        ArmorType type = ArmorType.matchType(e.getBrokenItem());
        if(type != null){
            Player p = e.getPlayer();
            ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.BROKE, type, e.getBrokenItem(), null);
            Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);
            if(armorEquipEvent.isCancelled()){
                ItemStack i = e.getBrokenItem().clone();
                i.setAmount(1);
                if (i.getItemMeta() instanceof Damageable damageable) {
                    damageable.setDamage(damageable.getDamage() + 1);
                    i.setItemMeta(damageable);
                }
                if(type.equals(ArmorType.HELMET)){
                    p.getInventory().setHelmet(i);
                }else if(type.equals(ArmorType.CHESTPLATE)){
                    p.getInventory().setChestplate(i);
                }else if(type.equals(ArmorType.LEGGINGS)){
                    p.getInventory().setLeggings(i);
                }else if(type.equals(ArmorType.BOOTS)){
                    p.getInventory().setBoots(i);
                }
            }
        }
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e){
        Player p = e.getEntity();
        if(e.getKeepInventory()) return;
        for(ItemStack i : p.getInventory().getArmorContents()){
            if(!isAirOrNull(i)){
                Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(p, ArmorEquipEvent.EquipMethod.DEATH, ArmorType.matchType(i), i, null));
                // No way to cancel a death event.
            }
        }
    }

    /**
     * A utility method to support versions that use null or air ItemStacks.
     */
    public static boolean isAirOrNull(ItemStack item){
        return item == null || item.getType().equals(Material.AIR);
    }
}
