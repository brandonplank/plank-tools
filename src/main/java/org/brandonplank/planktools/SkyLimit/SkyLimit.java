package org.brandonplank.planktools.SkyLimit;

import org.brandonplank.planktools.Main;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class SkyLimit implements Listener {
    private Main plugin;

    public SkyLimit(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        FileConfiguration config = this.plugin.getConfig();
        Player player = event.getPlayer();
        if(player.getWorld().getName().equals("world_nether")){
            Location loc = player.getLocation();
            Double nether_roof_height = 127.0;
            if(loc.getY() >= nether_roof_height){
                player.damage(1);
            }
        } else {
            if(player.isGliding()){
                Location loc = player.getLocation();
                Double max_fly_height = 1024.0;
                if(loc.getY() >= config.getDouble("max_fly_height")){
                    loc.setY(max_fly_height - 1);
                    player.teleport(loc);
                }
            }
        }
    }
}
