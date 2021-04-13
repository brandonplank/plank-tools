package org.brandonplank.planktools.Dupe;

import org.brandonplank.planktools.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class Dupe implements Listener {
    private final Main plugin;

    public Dupe(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPunch(EntityDamageByEntityEvent evt) {
        FileConfiguration config = this.plugin.getConfig();
        if (config.getBoolean("item_frame_dupe") &&
                evt.getEntityType() == EntityType.ITEM_FRAME) {
            double d = Math.random();
            if (((ItemFrame)evt.getEntity()).getItem().getType().toString().contains("SHULKER_BOX")) {
                if (d * 10.0D > 9.00D) {
                    evt.getEntity().getWorld().dropItemNaturally(evt.getEntity().getLocation(), ((ItemFrame)evt.getEntity()).getItem());
                    this.plugin.getLogger().info(evt.getDamager().getName() + " duped item");
                }
            } else if (d > 0.80D && d * 10.0D < 9.00D) {
                evt.getEntity().getWorld().dropItemNaturally(evt.getEntity().getLocation(), ((ItemFrame)evt.getEntity()).getItem());
                this.plugin.getLogger().info(evt.getDamager().getName() + " duped item");
            } else if (d * 10.0D > 9.99D) {
                evt.getEntity().getWorld().dropItemNaturally(evt.getEntity().getLocation(), ((ItemFrame)evt.getEntity()).getItem().asQuantity(((ItemFrame)evt.getEntity()).getItem().getMaxStackSize()));
                this.plugin.getLogger().info(evt.getDamager().getName() + " duped item");
            }
        }
    }

    @EventHandler
    public void PlayerDropItemEvent(PlayerDropItemEvent e){
        FileConfiguration config = this.plugin.getConfig();
        if(config.getBoolean("cauldron_dupe")){
            Player player = e.getPlayer();
            Item item = e.getItemDrop();
            Location loc = item.getLocation();
            if(item.getItemStack().getType().toString().contains("SHULKER_BOX") || config.getBoolean("dupe_all_items")){
                Location old = loc;
                loc.setY(loc.getY() -2);
                Material block = loc.getWorld().getBlockAt(loc).getBlockData().getMaterial();
                if(block.equals(Material.CAULDRON)){
                    player.getWorld().dropItemNaturally(old, item.getItemStack());
                } else {
                    // try once more to see if you are standing in it
                    loc.setY(loc.getY() + 1);
                    Material block1 = loc.getWorld().getBlockAt(loc).getBlockData().getMaterial();
                    if(block1.equals(Material.CAULDRON)){
                        player.getWorld().dropItemNaturally(old, item.getItemStack());
                    }
                }
            }
        }
    }
}
