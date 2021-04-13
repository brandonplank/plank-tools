package org.brandonplank.planktools.commands;

import org.brandonplank.planktools.ItemManager.ItemManager;
import org.brandonplank.planktools.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Commands implements CommandExecutor, Listener {

    public static HashMap<String, Boolean> map = new HashMap<String, Boolean>();

    public void mustBeAdmin(CommandSender sender){
        sender.sendMessage(ChatColor.RED + Main.getPlugin(Main.class).getConfig().getString("admin-message"));
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)){
            sender.sendMessage("Only players can send that command!");
            return true;
        }
        // Init player in hashmap database.
        Player player = (Player)sender;
        String playername = player.getName();

        if(command.getName().equalsIgnoreCase("illegalgive")){
            if(player.hasPermission("plank.illegalgive") || player.isOp()){
                if(args.length == 0){
                    sender.sendMessage("[plank-tools] Need a item cheif...");
                } else {
                    int amnt = 64;
                    if(args.length == 2){
                        amnt = Integer.parseInt(args[1]);
                    }
                    ItemStack item = new ItemStack(Material.getMaterial(args[0]), amnt);
                    if(item == null){
                        sender.sendMessage("[plank-tools] ERROR, UNKNOWN ITEM!!!!!!!");
                    } else {
                        player.getInventory().addItem(item);
                        sender.sendMessage("[plank-tools] Gave "+ amnt +"x " + args[0] +" to " + playername + "");
                    }
                }
            } else {
                mustBeAdmin(sender);
            }
            return true;
        } else if(command.getName().equalsIgnoreCase("givehammer")){
            if(player.hasPermission("plank.hammer") || player.isOp()){
                player.getInventory().addItem(ItemManager.Hammer);
            } else {
                mustBeAdmin(sender);
            }
            return true;
        } else if(command.getName().equalsIgnoreCase("givedeathstick")){
            if(player.hasPermission("plank.deathstick") || player.isOp()){
                player.getInventory().addItem(ItemManager.DeathStick);
            } else {
                mustBeAdmin(sender);
            }
            return true;
        } else if (command.getName().equalsIgnoreCase("pclip")) {
            if(player.hasPermission("plank.noclip") || player.isOp()) {

                //Make sure we are never null
                if(map.get(playername) == null){
                    // Init value to default false
                    map.put(playername, false);
                }
                if (!map.get(playername)) {
                    sender.sendMessage(ChatColor.GREEN + "[pclip] enabled!");
                    map.put(playername, true);
                } else {
                    sender.sendMessage(ChatColor.RED + "[pclip] disabled!");
                    map.put(playername, false);
                }
            } else {
                mustBeAdmin(sender);
            }
            return true;
        }
        return true;
    }
}
