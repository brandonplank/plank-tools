package org.brandonplank.planktools.RenderDistance;

import org.brandonplank.planktools.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class RenderDistance implements Listener {
    private final Main plugin;

    public HashMap<Player, Integer> renderdistance = new HashMap<>();

    public RenderDistance(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent evt) {
        double tps = Bukkit.getServer().getTPS()[0];
        if (this.plugin.getConfig().getBoolean("limit_render_distance_based_on_tps")) {
            if (tps > this.plugin.getConfig().getDouble("old_chunk_good_tps_radius")) {
                setRenderDistance(evt.getPlayer(), this.plugin.getConfig().getInt("chunk_good_tps_radius"));
            } else {
                setRenderDistance(evt.getPlayer(), this.plugin.getConfig().getInt("chunk_radius"));
            }
        }
    }

    private void setRenderDistance(Player p, int distance) {
        if (this.renderdistance.get(p) != null) {
            if (((Integer)this.renderdistance.get(p)).intValue() != distance) {
                p.getWorld().setViewDistance(Integer.valueOf(distance));
                this.renderdistance.put(p, Integer.valueOf(distance));
            }
        } else {
            p.getWorld().setViewDistance(Integer.valueOf(distance));
            this.renderdistance.put(p, Integer.valueOf(distance));
        }
    }
}
