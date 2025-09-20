package me.katze.powerantirelog.listener;

import me.katze.powerantirelog.event.PlayerEnterPvPEvent;
import me.katze.powerantirelog.manager.PvPManager;
import me.katze.powerantirelog.utility.PlayerUtility;
import org.bukkit.Bukkit;
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

    private void triggerPvPEvent(Player player1, Player player2) {
        if (player1 == null || player2 == null || player1 == player2) return;

        PlayerEnterPvPEvent event = new PlayerEnterPvPEvent(player1, player2);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            PvPManager.addPlayer(player1);
            PvPManager.addPlayer(player2);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        Player target = (Player) e.getEntity();
        Player damager = PlayerUtility.getDamager(e.getDamager());

        triggerPvPEvent(damager, target);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCombust(EntityCombustByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getCombuster() instanceof Player)) return;

        Player target = (Player) e.getEntity();
        Player damager = (Player) e.getCombuster();

        triggerPvPEvent(damager, target);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPotionSplash(PotionSplashEvent e) {
        if (e.getPotion() == null) return;
        if (!(e.getPotion().getShooter() instanceof Player)) return;

        Player damager = (Player) e.getPotion().getShooter();

        for (LivingEntity target : e.getAffectedEntities()) {
            if (target instanceof Player && target != damager) {
                for (PotionEffect effect : e.getPotion().getEffects()) {
                    if (effect.getType().equals(PotionEffectType.POISON)) {
                        triggerPvPEvent(damager, (Player) target);
                        break;
                    }
                }
            }
        }
    }
}
