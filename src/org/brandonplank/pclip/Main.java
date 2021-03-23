package org.brandonplank.pclip;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;

import java.util.HashMap;

public class Main extends JavaPlugin implements Listener {

    // this is good enough for now :)
    HashMap<String, Boolean> map = new HashMap<String, Boolean>();

    @Override
    public void onEnable(){
        this.getServer().getPluginManager().registerEvents(this, this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[pclip] loaded!");
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[pclip] unloaded!");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        //Make sure we are never null
        if(map.get(player.getName()) != null) {
            if (map.get(player.getName())) {
                if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                    if (nearBlock(player)) {
                        player.setGameMode(GameMode.SPECTATOR);
                    } else {
                        player.setGameMode(GameMode.CREATIVE);
                    }
                }
            }
        }
    }

    public boolean nearBlock(Player player) {
        boolean isNearBlock = false;
        BlockFace[] surrounding = new BlockFace[] {
                BlockFace.NORTH,
                BlockFace.NORTH_EAST,
                BlockFace.EAST,
                BlockFace.SOUTH_EAST,
                BlockFace.SOUTH,
                BlockFace.SOUTH_WEST,
                BlockFace.WEST,
                BlockFace.NORTH_WEST,
                BlockFace.UP,
                BlockFace.DOWN
        };
        Location[] locationsToCheck = new Location[] {player.getLocation(), player.getLocation().add(0.0D, 1.0D, 0.0D)};

        for (Location location : locationsToCheck) {
            for (BlockFace blockFace : surrounding) {
                if (!location.getBlock().getRelative(blockFace, 1).isEmpty()) {
                    isNearBlock = true;
                }
            }
        }

        return isNearBlock;
    }

    @Override
    public boolean onCommand(CommandSender sender,
                             Command command,
                             String label,
                             String[] args) {
        if (command.getName().equalsIgnoreCase("pclip")) {

            // Init player in hashmap database.
            Player senderplayer = (Player)sender;
            String playername = senderplayer.getName();

            //Make sure we are never null
            if(map.get(playername) == null){
                // Init value to default false
                map.put(playername, false);
            }
            if(senderplayer.isOp()) {
                if(!map.get(playername)){
                    sender.sendMessage(ChatColor.GREEN + "[pclip] enabled!");
                    map.put(playername, true);
                } else {
                    sender.sendMessage(ChatColor.RED + "[pclip] diabled!");
                    map.put(playername, false);
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Only a server admin can use this command!");
            }
            return true;
        }
        return false;
    }

}
