package org.brandonplank.planktools.ChunkFixer;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class FixChunk implements Listener {
    public static HashMap<String, Chunk> ChunkMap = new HashMap<String, Chunk>();
    public static Plugin PrivatePlugin = null;

    public static void init(Plugin plugin){
        PrivatePlugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Chunk lastchunk = ChunkMap.get(player.getName());
        Chunk chunk = player.getPlayer().getLocation().getChunk();

        if(lastchunk == null){
            lastchunk = ChunkMap.put(player.getName(), chunk);
        } else {
            String playerworld = player.getWorld().getName();
            if(!(chunk.getX() == lastchunk.getX() && chunk.getZ() == lastchunk.getZ())){
                ChunkMap.put(player.getName(), chunk);
                int posX = chunk.getX()*16;
                int posZ = chunk.getZ()*16;
                for(int x = 0; x <= 15; x++){
                    for(int z = 0; z <= 15; z++){
                        int newX = posX + x;
                        int newZ = posZ + z;
                        Block block = null;
                        Block block2 = null;
                        // 1.17 changes up ahead
                        if(PrivatePlugin.getServer().getVersion().contains("1.18")){
                            if(player.getWorld().getName().equals("world")){
                                block = PrivatePlugin.getServer().getWorld("world").getBlockAt(newX, -64 , newZ);
                            } else if(player.getWorld().getName().equals("world_nether")){
                                block = PrivatePlugin.getServer().getWorld("world_nether").getBlockAt(newX, 0 , newZ);
                                block2 = PrivatePlugin.getServer().getWorld("world_nether").getBlockAt(newX, 127 , newZ);
                            } else {
                                return;
                            }
                        } else {
                            if(player.getWorld().getName().equals("world")){
                                block = PrivatePlugin.getServer().getWorld("world").getBlockAt(newX, 0 , newZ);
                            } else if(player.getWorld().getName().equals("world_nether")){
                                block = PrivatePlugin.getServer().getWorld("world_nether").getBlockAt(newX, 0 , newZ);
                                block2 = PrivatePlugin.getServer().getWorld("world_nether").getBlockAt(newX, 127 , newZ);
                            } else {
                                return;
                            }
                        }
                        if(block.getType() != Material.BEDROCK){
                            block.setType(Material.BEDROCK);
                        }
                        if(playerworld.equals("world_nether")){
                            if(block2.getType() != Material.BEDROCK){
                                block2.setType(Material.BEDROCK);
                            }
                        }
                    }
                }
            }
        }
    }
}
