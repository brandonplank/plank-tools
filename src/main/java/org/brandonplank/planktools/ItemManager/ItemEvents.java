package org.brandonplank.planktools.ItemManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class ItemEvents implements Listener {
    @EventHandler
    public void RightClick(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if(!(player.getInventory().getItemInMainHand().getItemMeta() == null)){
            if (player.getInventory().getItemInMainHand().getItemMeta().equals(ItemManager.Hammer.getItemMeta())) {
                if(player.hasPermission("plank.hammer") || player.isOp()){
                    if(event.getRightClicked() instanceof Player){
                        String target = event.getRightClicked().getName();
                        player.sendMessage("Banned " + target);
                        player.performCommand("ban " + target + " You got whacked by le ban hammer");
                    }
                } else {
                    player.getInventory().removeItem(ItemManager.Hammer);
                    player.damage(10000);
                }
            } else if (player.getInventory().getItemInMainHand().getItemMeta().equals(ItemManager.DeathStick.getItemMeta())) {
                if(player.hasPermission("plank.deathstick") || player.isOp()){
                    if(event.getRightClicked() instanceof Player){
                        String target = event.getRightClicked().getName();
                        player.performCommand("kill " + target);
                    }
                } else {
                    player.getInventory().removeItem(ItemManager.DeathStick);
                    player.damage(10000);
                }
            }
        }
    }
}
