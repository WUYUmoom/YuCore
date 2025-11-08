package com.wuyumoom.yucore.api;

import com.wuyumoom.yucore.YuCore;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BukkitAPI {
    public static boolean hasInventorySpace(Player player) {
        return player.getInventory().firstEmpty() != -1;
    }
    private static final String regex = "^[\\u4e00-\\u9fa5（）：・]+$";
    public static boolean isPureChinese(String str) {
        return str.matches(regex);
    }
    public static @Nullable String[] onSetString(String s, String split) {
        if (s == null){
            return null;
        }
        String[] slotsArray = s.split(split);
        for (int i = 0; i < slotsArray.length; i++) {
            if (slotsArray[i].isEmpty()){
                slotsArray[i] = " ";
            }
        }
        return slotsArray;
    }
    public static int[] onSetInt(String s) {
        String[] slotsArray = s.split(",");
        int[] slot = new int[slotsArray.length];
        for (int i = 0; i < slotsArray.length; i++) {
            slot[i] = Integer.parseInt(slotsArray[i].trim());
        }
        return slot;
    }
    public static String onReplace(String s) {
        if (s == null){
            return "";
        }
        return s.replaceAll("&", "§");
    }

    public static List<String> onReplace(List<String> s) {
        if (s == null) {
            return List.of(); // 返回空列表或其他默认值
        }
        s.replaceAll(string -> string.replaceAll("&", "§"));
        return s;
    }

    public static Boolean hasThreeEmptySlots(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType().equals(Material.AIR)) {
                return true;
            }
        }
        return false;
    }

    public static TextComponent sendHoverMessage(String message, String hoverText) {
        TextComponent textComponent = sendHoverMessage(message);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
        return textComponent;
    }
    public static TextComponent sendHoverMessage(String message) {
        return new TextComponent(message);
    }
    public static void sendMessage(String message, Player player) {
        String[] strings = BukkitAPI.onSetString(message, ",");
        if (strings.length == 1){
            player.sendMessage(strings[0]);
            return;
        }
        if (strings.length == 6){
            if (strings[0].equalsIgnoreCase("Title")){
                player.sendTitle(strings[1], strings[2], Integer.parseInt(strings[3]), Integer.parseInt(strings[4]), Integer.parseInt(strings[5]));
                return;
            }
        }
        YuCore.getInstance().getServer().getLogger().info(message+"\n格式错误");
    }

}

