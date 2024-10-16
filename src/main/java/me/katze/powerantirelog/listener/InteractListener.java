package me.katze.powerantirelog.listener;

import me.katze.powerantirelog.AntiRelog;
import me.katze.powerantirelog.manager.PvPManager;
import me.katze.powerantirelog.utility.ColorUtility;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInteractWithEntity(PlayerInteractEntityEvent e) {
        if (!PvPManager.isPvP(e.getPlayer())) return;

        if (AntiRelog.getInstance().getConfig().getBoolean("settings.cancel.interact") && PvPManager.isPvP(e.getPlayer())) {
            e.getPlayer().sendMessage(ColorUtility.getMsg(AntiRelog.getInstance().getConfig().getString("messages.block")));
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!PvPManager.isPvP(e.getPlayer())) return;

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = e.getClickedBlock();

            if (block != null && block.getType() == Material.ENDER_CHEST) {
                e.getPlayer().sendMessage(ColorUtility.getMsg(AntiRelog.getInstance().getConfig().getString("messages.block")));
                e.setCancelled(true);
            }
        }
    }
}
