package me.katze.powerantirelog.listener;

import me.katze.powerantirelog.AntiRelog;
import me.katze.powerantirelog.data.CooldownData;
import me.katze.powerantirelog.manager.CooldownManager;
import me.katze.powerantirelog.manager.PvPManager;
import me.katze.powerantirelog.utility.ColorUtility;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.time.LocalTime;

public class CooldownListener implements Listener {

    @EventHandler
    public void onTotem(EntityResurrectEvent e) {
        if (e.getEntity() instanceof Player && e.isCancelled() == false) {
            if (e.getEntity().getEquipment() != null && (e.getEntity().getEquipment().getItemInMainHand().getType() == Material.TOTEM_OF_UNDYING
                    || e.getEntity().getEquipment().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING)) {
                Player player = ((Player) e.getEntity()).getPlayer();

                if (!PvPManager.isPvP(player)) return;

                CooldownData data = CooldownManager.getPlayer(player);
                ItemStack itemStack = null;

                ItemStack mainHandItem = player.getInventory().getItemInMainHand();
                if (mainHandItem.getType() == Material.TOTEM_OF_UNDYING) {
                    itemStack = mainHandItem;
                }

                ItemStack offHandItem = player.getInventory().getItemInOffHand();
                if (offHandItem.getType() == Material.TOTEM_OF_UNDYING) {
                    itemStack = offHandItem;
                }

                int configTime = AntiRelog.getInstance().getConfig().getInt("settings.cooldown.totem");

                if (data != null) {
                    LocalTime now = LocalTime.now();
                    LocalTime cooldown = data.getTime();

                    Duration timePassed = Duration.between(cooldown, now);
                    long secondsPassed = timePassed.getSeconds();
                    long remainingTime = configTime - secondsPassed;

                    if (secondsPassed >= configTime) {
                        CooldownManager.removePlayer(player);
                    } else {
                        player.sendMessage(ColorUtility.getMsg(AntiRelog.getInstance().getConfig().getString("messages.cooldown").replace("{time}", String.valueOf(remainingTime))));
                        if (AntiRelog.getInstance().getConfig().getString("messages.cooldown-subtitle") != null ||
                                (AntiRelog.getInstance().getConfig().getString("messages.cooldown-subtitle") != "")) {
                            player.sendTitle("", AntiRelog.getInstance().getConfig().getString("messages.cooldown-subtitle").replace("{time}", String.valueOf(remainingTime)), 6, 40, 6);
                        }
                        e.setCancelled(true);
                    }

                } else {
                    CooldownManager.addPlayer(player, itemStack);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e) {
        if (e.getItem() != null) {
            Player player = e.getPlayer();

            if (!PvPManager.isPvP(player)) return;

            CooldownData data = CooldownManager.getPlayer(player);
            ItemStack itemStack = e.getItem();
            int configTime = getCooldown(itemStack.getType());

            if (itemStack.getType() == Material.CHORUS_FRUIT || itemStack.getType() == Material.ENDER_PEARL) return;

            if (data != null) {
                if (configTime <= 0) return;

                LocalTime now = LocalTime.now();
                LocalTime cooldown = data != null ? data.getTime() : null;

                Duration timePassed = Duration.between(cooldown, now);
                long secondsPassed = timePassed.getSeconds();
                long remainingTime = configTime - secondsPassed;

                if (secondsPassed >= configTime) {
                    CooldownManager.removePlayer(player);
                } else {
                    player.sendMessage(ColorUtility.getMsg(AntiRelog.getInstance().getConfig().getString("messages.cooldown")
                            .replace("{time}", String.valueOf(remainingTime))));
                    if (AntiRelog.getInstance().getConfig().getString("messages.cooldown-subtitle") != null ||
                            (AntiRelog.getInstance().getConfig().getString("messages.cooldown-subtitle") != "")) {
                        player.sendTitle("", AntiRelog.getInstance().getConfig().getString("messages.cooldown-subtitle").replace("{time}", String.valueOf(remainingTime)), 6, 40, 6);
                    }
                    e.setCancelled(true);
                    return;
                }
            }
            CooldownManager.addPlayer(player, itemStack);
        }
    }

    @EventHandler
    public void onFireworkLaunch(ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof Firework) {
            if (e.getEntity().getShooter() instanceof Player) {
                Player player = (Player) e.getEntity().getShooter();

                if (!PvPManager.isPvP(player)) return;

                CooldownData data = CooldownManager.getPlayer(player);
                ItemStack itemStack = null;

                ItemStack mainHandItem = player.getInventory().getItemInMainHand();

                if (mainHandItem.getType() == Material.TOTEM_OF_UNDYING) {
                    itemStack = mainHandItem;
                }

                ItemStack offHandItem = player.getInventory().getItemInOffHand();
                if (offHandItem.getType() == Material.TOTEM_OF_UNDYING) {
                    itemStack = offHandItem;
                }

                if (data != null) {
                    LocalTime now = LocalTime.now();
                    LocalTime cooldown = data.getTime();
                    int configTime = AntiRelog.getInstance().getConfig().getInt("settings.cooldown.firework");

                    Duration timePassed = Duration.between(cooldown, now);
                    long secondsPassed = timePassed.getSeconds();
                    long remainingTime = configTime - secondsPassed;

                    if (secondsPassed >= configTime) {
                        CooldownManager.removePlayer(player);
                    } else {
                        player.sendMessage(ColorUtility.getMsg(AntiRelog.getInstance().getConfig().getString("messages.cooldown").replace("{time}", String.valueOf(remainingTime))));
                        if (AntiRelog.getInstance().getConfig().getString("messages.cooldown-subtitle") != null ||
                                (AntiRelog.getInstance().getConfig().getString("messages.cooldown-subtitle") != "")) {
                            player.sendTitle("", AntiRelog.getInstance().getConfig().getString("messages.cooldown-subtitle").replace("{time}", String.valueOf(remainingTime)), 6, 40, 6);
                        }
                        e.setCancelled(true);
                    }

                } else {
                    CooldownManager.addPlayer(player, itemStack);
                }
            }
        }
    }

    private int getCooldown(Material material) {
        switch (material) {
            case FIRE_CHARGE:
                return AntiRelog.getInstance().getConfig().getInt("settings.cooldown.firework");
            case GOLDEN_APPLE:
                return AntiRelog.getInstance().getConfig().getInt("settings.cooldown.golden-apple");
            case ENCHANTED_GOLDEN_APPLE:
                return AntiRelog.getInstance().getConfig().getInt("settings.cooldown.enchanted-golden-apple");
            case ENDER_PEARL:
                return AntiRelog.getInstance().getConfig().getInt("settings.cooldown.ender-pearl");
            case CHORUS_FRUIT:
                return AntiRelog.getInstance().getConfig().getInt("settings.cooldown.chorus");
            default:
                return 0;
        }
    }
}
