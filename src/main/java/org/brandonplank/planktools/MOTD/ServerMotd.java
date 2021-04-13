package org.brandonplank.planktools.MOTD;

import org.brandonplank.planktools.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;
import java.util.Random;

public class ServerMotd implements Listener {
    private Main plugin;

    public ServerMotd(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPing(ServerListPingEvent event) {
        if (!this.plugin.getConfig().getBoolean("system.enabled"))
            return;
        if (!this.plugin.getServer().hasWhitelist()) {
            List<String> systemmotdlist = this.plugin.getConfig().getStringList("system.motd.normal");
            Random random = new Random();
            String motd = systemmotdlist.get(random.nextInt(systemmotdlist.size()));
            motd = motd.replaceAll("&", "");
            motd = motd.replaceAll("%VERSION%", Bukkit.getServer().getBukkitVersion());
            event.setMotd(motd);
        } else {
            List<String> systemmotdlist = this.plugin.getConfig().getStringList("system.motd.whitelist");
            Random random = new Random();
            String motd = systemmotdlist.get(random.nextInt(systemmotdlist.size()));
            motd = motd.replaceAll("&", "");
            motd = motd.replaceAll("%VERSION%", Bukkit.getServer().getBukkitVersion());
            event.setMotd(motd);
        }
    }
}
