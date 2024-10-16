package me.katze.powerantirelog.listener;

import me.katze.powerantirelog.AntiRelog;
import me.katze.powerantirelog.manager.PvPManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.codemc.worldguardwrapper.event.WrappedDisallowedPVPEvent;

public class WorldGuardListener implements Listener {

    @EventHandler
    public void onPvP(WrappedDisallowedPVPEvent e) {
        if (AntiRelog.WORLDGUARD_HOOK != true) return;

        Player player1 = e.getAttacker();
        Player player2 = e.getDefender();

        if (PvPManager.isPvP(player1.getPlayer()) && PvPManager.isPvP(player2.getPlayer())) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }
    }
}
