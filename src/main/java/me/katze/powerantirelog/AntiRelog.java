package me.katze.powerantirelog;

import co.aikar.commands.PaperCommandManager;
import me.katze.powerantirelog.command.HelpCommand;
import me.katze.powerantirelog.command.ReloadCommand;
import me.katze.powerantirelog.command.TestCommand;
import me.katze.powerantirelog.listener.*;
import me.katze.powerantirelog.manager.CooldownManager;
import me.katze.powerantirelog.manager.PvPManager;
import me.katze.powerantirelog.utility.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class AntiRelog extends JavaPlugin {

    public static boolean CMI_HOOK = false;
    public static boolean ESSENTIALS_HOOK = false;
    public static boolean WORLDGUARD_HOOK = false;

    private static final int PLUGIN_ID = 23642;
    public static final String VERSION = "1.0-RELEASE";
    public static final String DISCORD_URL = "https://discord.gg/6wGy3sYxzw";

    private static AntiRelog instance;
    private FileConfiguration config;

    public PvPManager pvpmanager;
    public CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        instance = this;
        config = getConfig();

        System.out.println("Plugin version: " + VERSION);
        System.out.println("Support: " + DISCORD_URL);

        loadDepend();
        loadMetrics();
        loadConfig();

        loadListeners();
        loadCommands();

        pvpmanager = new PvPManager();
        cooldownManager = new CooldownManager();
    }

    @Override
    public void onDisable() {}

    private void loadCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(AntiRelog.getInstance());

        commandManager.registerCommand(new HelpCommand());
        commandManager.registerCommand(new ReloadCommand());
        commandManager.registerCommand(new TestCommand());
    }

    private void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new WorldGuardListener(), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new DeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new LeaveListener(), this);
        Bukkit.getPluginManager().registerEvents(new TeleportListener(), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new CommandListener(), this);
        Bukkit.getPluginManager().registerEvents(new CooldownListener(), this);
    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void loadMetrics() {
        Metrics metrics = new Metrics(this, PLUGIN_ID);
    }

    public void loadDepend() {
        if (getServer().getPluginManager().getPlugin("CMI") != null) {
            CMI_HOOK = true;
        }
        if (getServer().getPluginManager().getPlugin("Essentials") != null) {
            ESSENTIALS_HOOK = true;
        }
        if (getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            WORLDGUARD_HOOK = true;
        }
    }

    public static AntiRelog getInstance() {
        return instance;
    }
}
