package me.katze.powerantirelog.listener;

import me.katze.powerantirelog.AntiRelog;
import me.katze.powerantirelog.manager.PvPManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent e) {
        if (!PvPManager.isPvP(e.getPlayer())) return;

        PvPManager.leave(e.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onKick(PlayerKickEvent e) {
        if (!PvPManager.isPvP(e.getPlayer())) return;

       //for (String reasons : AntiRelog.getInstance().getConfig().getStringList("settings.leave.kill-reasons")) {
       //    if (e.getReason().contains(reasons)) {
       //        return;
       //    }
       //}

        PvPManager.leave(e.getPlayer());
    }
}
