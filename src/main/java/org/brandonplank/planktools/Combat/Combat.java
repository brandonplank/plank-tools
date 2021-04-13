package org.brandonplank.planktools.Combat;

import org.brandonplank.planktools.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Combat implements Listener {
    private Main plugin;

    FileConfiguration config = null;
    Integer cooldown = null;

    public Combat(Main plugin) {
        this.plugin = plugin;
        config = this.plugin.getConfig();
        cooldown = config.getInt("command_cooldown");
    }

    public HashMap<Player, Boolean> PlayerInCombat = new HashMap<>();
    public HashMap<Player, Integer> PlayerInCombatCountdown = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled=true)
    public void onDamageTaken(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            if (e.getDamage() > 0.1) {
                Player targetPlayer = ((Player) e.getEntity()).getPlayer();
                Player damagerPlayer = ((Player) e.getDamager()).getPlayer();
                putPlayerInCombat(targetPlayer, damagerPlayer);
            }
        }
    }

    private void putPlayerInCombat(Player target, Player damager){
        if(PlayerInCombat.get(target) == null){
            PlayerInCombat.put(target, false);
            PlayerInCombatCountdown.put(target, 0);
        }
        if(PlayerInCombat.get(damager) == null){
            PlayerInCombat.put(damager, false);
            PlayerInCombatCountdown.put(damager, 0);
        }
        // Now log hits

        Long delay = new Long(cooldown * 20);

        this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                // Prevent shit here
            }
        }, delay, delay);
    }
}
