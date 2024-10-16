package me.katze.powerantirelog.listener;

import me.katze.powerantirelog.AntiRelog;
import me.katze.powerantirelog.manager.PvPManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!PvPManager.isPvP(e.getPlayer())) return;
        if (AntiRelog.getInstance().getConfig().getBoolean("settings.protect")) {

            if (Bukkit.getServer().getTPS()[0] < 16) {
                return;
            }
            if (e.getPlayer().getPing() > 20000) {
                return;
            }
        }
        PvPManager.leave(e.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if (!PvPManager.isPvP(e.getPlayer())) return;

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.protect")) {

            if (Bukkit.getServer().getTPS()[0] < 16) {
                return;
            }
            if (e.getPlayer().getPing() > 20000) {
                return;
            }
        }

        for (String reasons : AntiRelog.getInstance().getConfig().getStringList("settings.leave.kill-reasons")) {
            if (e.getReason().contains(reasons)) {
                return;
            }
        }

        PvPManager.leave(e.getPlayer());
    }
}
