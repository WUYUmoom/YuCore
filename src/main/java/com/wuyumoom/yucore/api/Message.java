package com.wuyumoom.yucore.api;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Message {
    private Map<String,String> message = new HashMap<>();
    public Message(FileConfiguration config){
        if (config.contains("Message") && config.isConfigurationSection("Message")) {
            for (String key : config.getConfigurationSection("Message").getKeys(false)) {
                message.put(key, BukkitAPI.onReplace(config.getString("Message." + key)));
            }
        }
    }

    public Map<String, String> getMessage() {
        return message;
    }
    public void setMessage(String key, Player player,String replace, String v){
        String s = message.get(key);
        if (s == null){
            BukkitAPI.sendMessage("§c消息不存在", player);
            return;
        }
        BukkitAPI.sendMessage(s.replaceAll(replace, v), player);
    }
    public void sendMessage(String key, Player player){
        String s = message.get(key);
        if (s == null){
            BukkitAPI.sendMessage("§c消息不存在", player);
            return;
        }
        BukkitAPI.sendMessage(message.get(key), player);
    }
}
