package me.katze.powerantirelog.manager;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import me.katze.powerantirelog.AntiRelog;
import me.katze.powerantirelog.utility.BossBarUtility;
import me.katze.powerantirelog.utility.ColorUtility;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PvPManager {
    private static HashMap<String, Integer> pvpMap;

    public PvPManager() {
        this.pvpMap = new HashMap<>();
        startTask();
    }

    public static void addPlayer(Player player) {
        String name = player.getName();
        int time = AntiRelog.getInstance().getConfig().getInt("settings.time");

        if (player == null) return;
        if (player.getType() != EntityType.PLAYER) return;

        if (player.hasPermission("powerantirelog.bypass")) return;
        if (player.isOp()) return;
        if (player.isInvulnerable()) return;

        for (String world : AntiRelog.getInstance().getConfig().getStringList("settings.disabled-worlds")) {
            if (player.getWorld().getName() == world) {
                return;
            }
        }

        if (pvpMap.containsKey(name)) {
            pvpMap.replace(name, pvpMap.get(name), time);
        } else {
            player.sendMessage(ColorUtility.getMsg(AntiRelog.getInstance().getConfig().getString("messages.start")));

            disable(player);
            pvpMap.put(name, time);
        }
    }

    public static boolean isPvP(Player player) {
        String name = player.getName();

        if (pvpMap.containsKey(name)) {
            return true;
        }
        return false;
    }

    public static void leave(Player player) {
        if (AntiRelog.getInstance().getConfig().getBoolean("settings.leave.kill")) {
            player.damage(player.getHealth());
        }

        for (String string : AntiRelog.getInstance().getConfig().getStringList("settings.leave.message")) {
            String replacedString = ColorUtility.getMsg(string).replace("{player}", player.getName());
            Bukkit.getServer().broadcastMessage(replacedString);
        }


        pvpMap.remove(player.getName());
    }

    private static void disable(Player player) {
        if (AntiRelog.getInstance().getConfig().getBoolean("settings.disable.fly")) {
            if (player.isFlying()) player.setFlying(false);
        }

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.disable.speed")) {
            if (player.getWalkSpeed() != 0.2F) player.setWalkSpeed(0.2F);
        }

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.disable.gamemode")) {
            if (player.getGameMode() != GameMode.SURVIVAL) player.setGameMode(GameMode.SURVIVAL);
        }

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.disable.invisibility")) {
            if (player.getActivePotionEffects().contains(PotionEffectType.INVISIBILITY)) player.removePotionEffect(PotionEffectType.INVISIBILITY);
        }

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.disable.elytra")) {
            if (player.getInventory().getChestplate().getType() != Material.ELYTRA) {
                ItemStack itemStack = player.getInventory().getChestplate();
                player.getInventory().getChestplate().setType(null);
                player.getInventory().addItem(itemStack);
            }
        }

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.disable.godmode")) {
            if (AntiRelog.CMI_HOOK == true) {
                CMIUser user = CMI.getInstance().getPlayerManager().getUser(player);

                if (user.isGod()) user.setTgod(0);
            }
            if (AntiRelog.ESSENTIALS_HOOK == true) {
                Essentials essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");

                User user = essentials.getUser(player);

                if (user.isGodModeEnabled()) user.setGodModeEnabled(false);
                if (user.isGodModeEnabled()) user.setGodModeEnabled(false);
            }
        }
    }

    public static void startTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        }.runTaskTimer(AntiRelog.getInstance(), 0L, 20L);
    }

    private static void update() {
        if (pvpMap == null) return;

        Iterator<Map.Entry<String, Integer>> iterator = pvpMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            String name = entry.getKey();
            int time = entry.getValue() - 1;

            if (time <= 0) {
                iterator.remove();
                Player player = Bukkit.getPlayer(name);
                if (player != null) {
                    player.sendMessage(ColorUtility.getMsg(AntiRelog.getInstance().getConfig().getString("messages.end")));
                }
            } else {
                pvpMap.replace(name, time);
                BossBarUtility.set(name, time);
            }
        }
    }

    public static void death(Player player) {
        String name = player.getName();

        if (pvpMap.containsKey(name)) {
            pvpMap.remove(name);
        }
    }
}
