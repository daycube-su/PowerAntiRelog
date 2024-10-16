package me.katze.powerantirelog.listener;

import me.katze.powerantirelog.AntiRelog;
import me.katze.powerantirelog.manager.PvPManager;
import me.katze.powerantirelog.utility.DamagerUtility;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        Player target = (Player) e.getEntity();
        Player damager = DamagerUtility.getDamager(e.getDamager());

        if (damager == null) return;
        PvPManager.addPlayers(target, damager);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInteractWithEntity(PlayerInteractEntityEvent e) {
        if (AntiRelog.getInstance().getConfig().getBoolean("settings.cancel.interact") && PvPManager.isPvP(e.getPlayer())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCombust(EntityCombustByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player target = (Player) e.getEntity();
        Player damager = DamagerUtility.getDamager(e.getCombuster());

        if (damager == null) return;
        PvPManager.addPlayers(target, damager);
    }
}
