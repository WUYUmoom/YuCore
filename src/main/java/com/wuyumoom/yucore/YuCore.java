package com.wuyumoom.yucore;

import com.wuyumoom.yucobblemonapi.lang.CobblemonLang;
import com.wuyumoom.yucore.api.Message;
import com.wuyumoom.yucore.lang.LangLoad;
import com.wuyumoom.yucore.papi.Papi;
import com.wuyumoom.yucore.yulistener.PluginListener;
import com.wuyumoom.yucore.yulistener.PokemonLabelEvent;
import net.fabricmc.loader.api.FabricLoader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class YuCore extends JavaPlugin {
    public static YuCore instance;
    private static CobblemonLang cobblemon;
    private static boolean isTranslatePath;
    private static Map<String,String> message;
    @Override
    public void onEnable() {
        instance = this;
        cobblemon = new CobblemonLang();
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        isTranslatePath = config.getBoolean("TranslatePath",false);
        message = new Message(config).getMessage();
        Bukkit.getPluginManager().registerEvents(new PluginListener(), this);
        new Papi().register();
        onMessage();
    }
    private void onMessage() {
        Bukkit.getConsoleSender().sendMessage("==================================================================================");
        Bukkit.getConsoleSender().sendMessage("§c __   __      ____               ");
        Bukkit.getConsoleSender().sendMessage("§c \\ \\ / /   _ / ___|___  _ __ ___ ");
        Bukkit.getConsoleSender().sendMessage("§c  \\ V / | | | |   / _ \\| '__/ _ \\");
        Bukkit.getConsoleSender().sendMessage("§c   | || |_| | |__| (_) | | |  __/");
        Bukkit.getConsoleSender().sendMessage("§c   |_| \\__,_|\\____\\___/|_|  \\___|");
        if (FabricLoader.getInstance().isModLoaded("cobblemon")) {
            new PokemonLabelEvent();
            Bukkit.getConsoleSender().sendMessage("§b已启动方可萌API");
            cobblemon.getZh();
            LangLoad.loadAbilities();
            LangLoad.loadSpecies();
            LangLoad.loadNature();
            LangLoad.loadPokeBall();
            LangLoad.loadMove();
            LangLoad.loadStat();
            LangLoad.loadGender();
        }
        Bukkit.getConsoleSender().sendMessage("§e§l语之宝可梦API §6§l启动完成！");
        Bukkit.getConsoleSender().sendMessage("§e§l作者 : 姬无语 §6§lQQ1841375451");
        Bukkit.getConsoleSender().sendMessage("==================================================================================");
    }

    public static YuCore getInstance() {
        return instance;
    }

    public static CobblemonLang getCobblemon() {
        return cobblemon;
    }

    public static Map<String, String> getMessage() {
        return message;
    }

    public static boolean isIsTranslatePath() {
        return isTranslatePath;
    }

    public static void setIsTranslatePath(boolean isTranslatePath) {
        YuCore.isTranslatePath = isTranslatePath;
    }
}
