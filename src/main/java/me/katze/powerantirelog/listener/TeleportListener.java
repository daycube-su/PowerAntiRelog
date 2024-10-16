package me.katze.powerantirelog.listener;

import me.katze.powerantirelog.AntiRelog;
import me.katze.powerantirelog.manager.PvPManager;
import net.ess3.api.events.teleport.PreTeleportEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent e) {
        PlayerTeleportEvent.TeleportCause cause = e.getCause();

        if (!PvPManager.isPvP(e.getPlayer())) return;

        if (cause == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT
                && AntiRelog.getInstance().getConfig().getBoolean("settings.cancel.chorus")) {
            e.setCancelled(true);
            return;
        }

        if (cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL
                && AntiRelog.getInstance().getConfig().getBoolean("settings.cancel.ender-pearl")) {

            e.setCancelled(true);
            return;
        }

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.cancel.teleport")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPreTeleport(PreTeleportEvent e) {
        if (!PvPManager.isPvP(e.getTeleporter().getBase())) return;

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.cancel.teleport")) {
            e.setCancelled(true);
        }
    }
}
