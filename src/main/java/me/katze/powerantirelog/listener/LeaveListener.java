package me.katze.powerantirelog.listener;

import me.katze.powerantirelog.manager.PvPManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public void on(PlayerQuitEvent e) {
        PvPManager.leave(e.getPlayer());
    }
}
