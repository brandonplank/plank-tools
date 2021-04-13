package org.brandonplank.planktools;

import org.brandonplank.planktools.ChunkFixer.FixChunk;
import org.brandonplank.planktools.Dupe.Dupe;
import org.brandonplank.planktools.ItemManager.IllegalItemRemover;
import org.brandonplank.planktools.ItemManager.ItemEvents;
import org.brandonplank.planktools.ItemManager.ItemManager;
import org.brandonplank.planktools.MOTD.ServerMotd;
import org.brandonplank.planktools.RenderDistance.RenderDistance;
import org.brandonplank.planktools.SkyLimit.SkyLimit;
import org.brandonplank.planktools.commands.Commands;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;


public class Main extends JavaPlugin implements Listener {
    public static final Integer[] old_total = new Integer[] { Integer.valueOf(0) };

    public static final Integer[] new_total = new Integer[] { Integer.valueOf(0) };

    public final HashMap<Player, Integer> newChunks = new HashMap<>();

    public final HashMap<Player, Integer> oldChunks = new HashMap<>();

    public HashMap<Player, Boolean> PlayerInCombat = new HashMap<>();

    public HashMap<Player, Integer> levels = new HashMap<>();



    @Override
    public void onEnable(){
        PluginManager manager = this.getServer().getPluginManager();
        saveDefaultConfig();
        ItemManager.init();
        FixChunk.init(this);
        manager.registerEvents(this, this);
        manager.registerEvents(new ItemEvents(), this);
        manager.registerEvents(new FixChunk(), this);
        manager.registerEvents(new Commands(), this);
        manager.registerEvents(new ServerMotd(this), this);
        manager.registerEvents(new IllegalItemRemover(this), this);
        manager.registerEvents(new RenderDistance(this), this);
        manager.registerEvents(new Dupe(this), this);
        manager.registerEvents(new SkyLimit(this), this);
        getCommand("givehammer").setExecutor(new Commands());
        getCommand("givedeathstick").setExecutor(new Commands());
        getCommand("pclip").setExecutor(new Commands());
        getCommand("illegalgive").setExecutor(new Commands());
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[plank-tools] Loaded!");
    }

    @Override
    public void onDisable(){
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[plank-tools] Unloaded!");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        //Make sure we are never null
        if(Commands.map.get(player.getName()) != null) {
            if (Commands.map.get(player.getName())) {
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
}