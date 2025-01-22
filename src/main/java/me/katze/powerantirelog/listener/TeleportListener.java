package me.katze.powerantirelog.listener;

import me.katze.powerantirelog.AntiRelog;
import me.katze.powerantirelog.data.CooldownData;
import me.katze.powerantirelog.manager.CooldownManager;
import me.katze.powerantirelog.manager.PvPManager;
import me.katze.powerantirelog.utility.ColorUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.time.LocalTime;

public class TeleportListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent e) {
        PlayerTeleportEvent.TeleportCause cause = e.getCause();
        Player player = e.getPlayer();

        if (!PvPManager.isPvP(player)) return;

        if (cause == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT || cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            CooldownData data = CooldownManager.getPlayer(player);
            int configTime = (cause == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT)
                    ? AntiRelog.getInstance().getConfig().getInt("settings.cooldown.chorus")
                    : AntiRelog.getInstance().getConfig().getInt("settings.cooldown.ender-pearl");

            ItemStack itemStack = null;

            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            if (mainHandItem.getType() == ((cause == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) ? Material.CHORUS_FRUIT : Material.ENDER_PEARL)) {
                itemStack = mainHandItem;
            }

            ItemStack offHandItem = player.getInventory().getItemInOffHand();
            if (offHandItem.getType() == ((cause == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) ? Material.CHORUS_FRUIT : Material.ENDER_PEARL)) {
                itemStack = offHandItem;
            }

            if (data != null) {
                if (configTime <= 0) return;

                LocalTime now = LocalTime.now();
                LocalTime cooldown = data.getTime();

                Duration timePassed = Duration.between(cooldown, now);
                long secondsPassed = timePassed.getSeconds();
                long remainingTime = configTime - secondsPassed;

                if (secondsPassed < configTime) {
                    player.sendMessage(ColorUtility.getMsg(AntiRelog.getInstance().getConfig().getString("messages.cooldown")
                            .replace("{time}", String.valueOf(remainingTime))));
                    if (AntiRelog.getInstance().getConfig().getString("messages.cooldown-subtitle") != null ||
                            (AntiRelog.getInstance().getConfig().getString("messages.cooldown-subtitle") != "")) {
                        player.sendTitle("", AntiRelog.getInstance().getConfig().getString("messages.cooldown-subtitle").replace("{time}", String.valueOf(remainingTime)), 6, 40, 6);
                    }
                    e.setCancelled(true);

                    if (itemStack != null) {
                        itemStack.setAmount(itemStack.getAmount() + 1);
                    }

                    return;
                } else {
                    CooldownManager.removePlayer(player);
                }
            }

            CooldownManager.addPlayer(player, itemStack);
            return;
        }

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.cancel.teleport")) {
            e.setCancelled(true);
        }
    }
}
