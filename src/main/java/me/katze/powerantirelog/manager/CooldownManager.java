package me.katze.powerantirelog.manager;

import me.katze.powerantirelog.data.CooldownData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalTime;
import java.util.HashMap;

public class CooldownManager {
    private static HashMap<String, CooldownData> cooldownMap;

    public CooldownManager() {
        this.cooldownMap = new HashMap<>();
    }

    public static void addPlayer(Player player, ItemStack itemStack) {
        if (itemStack == null) return;

        CooldownData data = new CooldownData(LocalTime.now(), itemStack.getItemMeta().getLocalizedName());

        cooldownMap.put(player.getName(), data);
    }

    public static CooldownData getPlayer(Player player) {
        if (cooldownMap.containsKey(player.getName())) {
            return cooldownMap.get(player.getName());
        }
        return null;
    }

    public static void removePlayer(Player player) {
        cooldownMap.remove(player.getName());
    }

    public static void clearMap() {
        cooldownMap.clear();
    }
}
