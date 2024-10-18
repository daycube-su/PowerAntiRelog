package me.katze.powerantirelog.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.katze.powerantirelog.AntiRelog;
import me.katze.powerantirelog.manager.PvPManager;
import me.katze.powerantirelog.utility.ColorUtility;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("antirelog|ar|powerantirelog")
public class TestCommand extends BaseCommand {

    @Subcommand("test")
    @CommandPermission("powerantirelog.test")
    public void onTest(CommandSender sender, @Optional String target) {
        Player player = Bukkit.getPlayer(target);

        if (player == null) {
            sender.sendMessage(ColorUtility.getMsg(AntiRelog.getInstance().getConfig().getString("messages.not-found")).replace("{player}", target));
            return;
        }

        PvPManager.addPlayer(player);

        sender.sendMessage(ColorUtility.getMsg(AntiRelog.getInstance().getConfig().getString("messages.test")).replace("{player}", target));
    }
}