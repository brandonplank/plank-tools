package org.brandonplank.planktools.ItemManager;

import org.brandonplank.planktools.Main;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class IllegalItemRemover implements Listener {
    private Main plugin = null;

    public IllegalItemRemover(Main plugin){
        this.plugin = plugin;
    }

    private HashMap<String, Player> playermap = new HashMap<>();

    @EventHandler
    private void onPlayerInteractEvent(PlayerInteractEvent evt) {
        FileConfiguration config = this.plugin.getConfig();
        if (evt.getClickedBlock() != null && evt.getClickedBlock().getState() instanceof Container) {
            ((Container)evt.getClickedBlock().getState()).getInventory().forEach(this::revert);
            config.getList("banned_blocks").forEach(b -> ((Container)evt.getClickedBlock().getState()).getInventory().remove(Material.getMaterial((String)b)));
        }
    }

    @EventHandler
    private void onPlayerJoinEvent(PlayerJoinEvent evt) {
        if(evt.getPlayer().isOp()){
            playermap.put(evt.getPlayer().getName(), evt.getPlayer());
        }
        FileConfiguration config = this.plugin.getConfig();
        config.getList("banned_blocks").forEach(b -> {
            evt.getPlayer().getInventory().remove(Material.getMaterial((String) b));
        });
        evt.getPlayer().getInventory().forEach(this::revert);
    }

    @EventHandler
    private void onInventoryOpenEvent(InventoryOpenEvent evt) {
        FileConfiguration config = this.plugin.getConfig();
        config.getList("banned_blocks").forEach(b -> {
            evt.getPlayer().getInventory().remove(Material.getMaterial((String)b));
        });
        evt.getPlayer().getInventory().forEach(this::revert);
    }

    @EventHandler
    private void onBlockDropItemEvent(PlayerDropItemEvent evt) {
        FileConfiguration config = this.plugin.getConfig();
        revert(evt.getItemDrop().getItemStack());
        evt.getPlayer().getInventory().forEach(this::revert);
    }

    @EventHandler
    private void onInventoryInteractEvent(InventoryInteractEvent evt) {
        FileConfiguration config = this.plugin.getConfig();
        config.getList("banned_blocks").forEach(b -> {
            if (evt.getInventory().contains(Material.getMaterial((String)b))) {
                evt.getInventory().remove(Material.getMaterial((String)b));
                evt.setCancelled(true);
            }
        });
        evt.getInventory().forEach(this::revert);
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent evt) {
        FileConfiguration config = this.plugin.getConfig();
        revert(evt.getCurrentItem());
        evt.getWhoClicked().getInventory().forEach(this::revert);
    }

    @EventHandler
    private void onInventoryPickup(InventoryPickupItemEvent evt) {
        FileConfiguration config = this.plugin.getConfig();
        String item = evt.getItem().getType().toString();
        if (config.getList("banned_blocks").contains(item))
            evt.setCancelled(true);
        evt.getInventory().forEach(this::revert);
    }

    @EventHandler
    private void onInventoryMove(InventoryMoveItemEvent evt) {
        FileConfiguration config = this.plugin.getConfig();
        revert(evt.getItem());
        evt.getSource().forEach(this::revert);
        evt.getDestination().forEach(this::revert);
        evt.getInitiator().forEach(this::revert);
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent evt) {
        FileConfiguration config = this.plugin.getConfig();
        if (config.getBoolean("prevent_illegal_block_place")) {
            String block = evt.getBlockPlaced().getType().toString();
            if (config.getList("banned_blocks").contains(block)) {
                config.getList("banned_blocks").forEach(b -> {
                    if (evt.getPlayer().getInventory().contains(Material.getMaterial((String)b))) {
                        evt.getPlayer().getInventory().remove(Material.getMaterial((String)b));
                        evt.setCancelled(true);
                    }
                });
                evt.setCancelled(true);
            }
            evt.getPlayer().getInventory().forEach(this::revert);
        }
    }

    public void revert(ItemStack c) {
        if (c != null) {
            if (plugin.getConfig().getBoolean("revert_stacked_items")) {
                if (plugin.getConfig().getBoolean("revert_some_stacked_items")) {
                    for (String s : plugin.getConfig().getStringList("revert_item_list")) {
                        if (c.getType().name().equals(s)) {
                            if (c.getAmount() > c.getMaxStackSize()) {
                                c.setAmount(c.getMaxStackSize());
                            }
                        }
                    }
                } else {
                    if (c.getAmount() > c.getMaxStackSize()) {
                        c.setAmount(c.getMaxStackSize());
                    }
                }
            }
            if (plugin.getConfig().getBoolean("revert_enchantments")) {
                for (Map.Entry<Enchantment, Integer> e : c.getEnchantments().entrySet()) {
                    if (e.getValue() != null && e.getKey() != null) {
                        if (e.getValue() > e.getKey().getMaxLevel()) {
                            c.subtract();
                        }
                    }
                }
            }
            if (plugin.getConfig().getList("banned_blocks").contains(c.getType().name())) {
                c.setAmount(0);
            }
        }
    }
}
