package me.katze.powerantirelog.manager;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.EssentialsUpgrade;
import com.earth2me.essentials.User;
import me.katze.powerantirelog.AntiRelog;
import me.katze.powerantirelog.utility.ColorUtility;
import net.ess3.api.IUser;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.time.LocalTime;
import java.util.HashMap;

public class PvPManager {
    private static HashMap<String, LocalTime> pvpMap;

    public static void addPlayers(Player player1, Player player2) {
        String name1 = player1.getName();
        String name2 = player2.getName();
        LocalTime currentTime = LocalTime.now();

        if (player1.hasPermission("powerantirelog.bypass")) return;
        if (player2.hasPermission("powerantirelog.bypass")) return;

        disable(player1, player2);

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.close-inventory")) {
            player1.closeInventory();
            player2.closeInventory();
        }

        if (pvpMap.containsKey(name1) || pvpMap.containsKey(name2)) {
            pvpMap.replace(name1, pvpMap.get(name1), currentTime);
            pvpMap.replace(name2, pvpMap.get(name2), currentTime);
        } else {
            pvpMap.put(name1, currentTime);
            pvpMap.put(name2, currentTime);
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
        if (AntiRelog.getInstance().getConfig().getBoolean("settings.leave.enabled")) return;

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.leave.kill")) {
            player.damage(player.getHealth());
        }

        if (AntiRelog.getInstance().getConfig().getStringList("settings.leave.message") != null) {
            for (String string : AntiRelog.getInstance().getConfig().getStringList("settings.leave.message")) {
                String replacedString = ColorUtility.getMsg(string).replace("{player}", player.getName());
                Bukkit.getServer().broadcastMessage(replacedString);
            }
        }

        pvpMap.remove(player.getName());
    }

    private static void disable(Player player1, Player player2) {
        if (AntiRelog.getInstance().getConfig().getBoolean("settings.disable.fly")) {
            if (player1.isFlying()) player1.setFlying(false);
            if (player2.isFlying()) player2.setFlying(false);
        }

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.disable.speed")) {
            if (player1.getWalkSpeed() != 0.1F) player1.setWalkSpeed(0.1F);
            if (player2.getWalkSpeed() != 0.1F) player2.setWalkSpeed(0.1F);
        }

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.disable.gamemode")) {
            if (player1.getGameMode() != GameMode.SURVIVAL) player1.setGameMode(GameMode.SURVIVAL);
            if (player2.getGameMode() != GameMode.SURVIVAL) player2.setGameMode(GameMode.SURVIVAL);
        }

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.disable.invisibility")) {
            if (player1.getActivePotionEffects().contains(PotionEffectType.INVISIBILITY)) player1.removePotionEffect(PotionEffectType.INVISIBILITY);
            if (player2.getActivePotionEffects().contains(PotionEffectType.INVISIBILITY)) player2.removePotionEffect(PotionEffectType.INVISIBILITY);
        }

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.disable.elytra")) {
            if (player1.getInventory().getChestplate().getType() != Material.ELYTRA) {
                ItemStack itemStack = player1.getInventory().getChestplate();
                player1.getInventory().getChestplate().setType(null);
                player1.getInventory().addItem(itemStack);
            }

            if (player2.getInventory().getChestplate().getType() != Material.ELYTRA) {
                ItemStack itemStack = player2.getInventory().getChestplate();
                player2.getInventory().getChestplate().setType(null);
                player2.getInventory().addItem(itemStack);
            }
        }

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.disable.godmode")) {
            if (AntiRelog.CMI_HOOK == true) {
                CMIUser user1 = CMI.getInstance().getPlayerManager().getUser(player1);
                CMIUser user2 = CMI.getInstance().getPlayerManager().getUser(player2);

                if (user1.isGod()) user1.setGod(false);
                if (user2.isGod()) user2.setGod(false);
            }
            if (AntiRelog.ESSENTIALS_HOOK == true) {
                Essentials essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");

                User user1 = essentials.getUser(player1);
                User user2 = essentials.getUser(player2);

                if (user1.isGodModeEnabled()) user1.setGodModeEnabled(false);
                if (user2.isGodModeEnabled()) user2.setGodModeEnabled(false);
            }
        }
    }



}
