package me.katze.powerantirelog.listener;

import me.katze.powerantirelog.AntiRelog;
import me.katze.powerantirelog.manager.PvPManager;
import me.katze.powerantirelog.utility.DamagerUtility;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity().getType() != EntityType.PLAYER) {
            return;
        }

        Player target = (Player) e.getEntity();
        Player damager = DamagerUtility.getDamager(e.getDamager());

        if (damager == null) return;

        if (!target.hasPermission("powerantirelog.bypass")) {
            PvPManager.addPlayer(target);
        }
        if (!damager.hasPermission("powerantirelog.bypass")) {
            PvPManager.addPlayer(damager);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCombust(EntityCombustByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player target = (Player) e.getEntity();
        Player damager = DamagerUtility.getDamager(e.getCombuster());

        if (damager == null) return;

        if (!target.hasPermission("powerantirelog.bypass")) {
            PvPManager.addPlayer(target);
        }
        if (!damager.hasPermission("powerantirelog.bypass")) {
            PvPManager.addPlayer(damager);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPotionSplash(PotionSplashEvent e) {
        if (e.getPotion() != null && e.getPotion().getShooter() instanceof Player) {
            Player damager = (Player) e.getPotion().getShooter();
            for (LivingEntity target : e.getAffectedEntities()) {
                if (target.getType() == EntityType.PLAYER && target != damager) {
                    for (PotionEffect effect : e.getPotion().getEffects()) {
                        if (effect.getType().equals(PotionEffectType.POISON)) {

                            if (!damager.hasPermission("powerantirelog.bypass")) {
                                PvPManager.addPlayer(damager);
                            }
                            if (!target.hasPermission("powerantirelog.bypass")) {
                                PvPManager.addPlayer((Player) target);
                            }
                        }
                    }
                }
            }
        }
    }
}
