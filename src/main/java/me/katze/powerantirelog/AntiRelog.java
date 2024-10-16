package me.katze.powerantirelog;

import me.katze.powerantirelog.utility.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class AntiRelog extends JavaPlugin {
    private static final String LOGO = "/*" +
            "\n█▄▀ ▄▀█ ▀█▀ ▀█ █▀▀ ▄▄ ▄▀█ █▄░█ ▀█▀ █ █▄▄ █▀█ ▀█▀"
            + "\n█░█ █▀█ ░█░ █▄ ██▄ ░░ █▀█ █░▀█ ░█░ █ █▄█ █▄█ ░█░"
            + "\nhttps://discord.gg/6wGy3sYxzw"
            + "\n*/";
    private static final int PLUGIN_ID = 23642;
    public static final String VERSION = "0.1-Beta";

    private static AntiRelog instance;
    private static final Logger LOGGER = Logger.getLogger("PowerAntiRelog");
    private FileConfiguration config;

    @Override
    public void onEnable() {
        instance = this;
        config = getConfig();

        loadMetrics();
        loadConfig();

        loadListeners();
        loadCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void loadCommands() {

    }

    private void loadListeners() {

    }

    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void loadMetrics() {
        Metrics metrics = new Metrics(this, PLUGIN_ID);
    }

    public static AntiRelog getInstance() {
        return instance;
    }
}
