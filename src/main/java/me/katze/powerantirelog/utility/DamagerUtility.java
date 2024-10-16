package me.katze.powerantirelog.utility;

import org.bukkit.entity.*;

public class DamagerUtility {

    public static Player getDamager(Entity damager) {
        if (damager instanceof Player) {
            return (Player) damager;
        } else if (damager instanceof Projectile) {
            Projectile projectile = (Projectile) damager;
            if (projectile.getShooter() instanceof Player) {
                return (Player) projectile.getShooter();
            }
        } else if (damager instanceof TNTPrimed) {
            TNTPrimed tntPrimed = (TNTPrimed) damager;
            return getDamager(tntPrimed.getSource());
        }
        return null;
    }
}
