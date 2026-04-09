package com.wuyumoom.yucore.yulistener;

import com.wuyumoom.yucore.YuCore;
import com.wuyumoom.yucore.task.Advertisement;
import com.wuyumoom.yucore.view.AbstractUI;
import com.wuyumoom.yucore.view.GuiSession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.ServerLoadEvent;

public class PluginListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event){
        if (event.getWhoClicked().getOpenInventory().getTopInventory().getHolder() instanceof AbstractUI abstractUI) {
            abstractUI.onClick(event.getSlot(), event);
        }
        if (event.getWhoClicked().getOpenInventory().getTopInventory().getHolder() instanceof GuiSession guiSession) {
            for (var callback : guiSession.getClickCallbacks()) {
                callback.invoke(event);
            }
        }
    }
    @EventHandler
    public void onClose(InventoryOpenEvent event){
        if (event.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof GuiSession guiSession){
            for (var callback : guiSession.getCloseCallbacks()) {
                callback.invoke();
            }
        }
    }
    @EventHandler
    public void onOpen(InventoryOpenEvent event){
        if (event.getPlayer().getOpenInventory().getTopInventory().getHolder() instanceof GuiSession guiSession) {
            for (var callback : guiSession.getOpenCallbacks()) {
                callback.invoke();
            }
        }
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        Advertisement.startTimer(YuCore.getInstance());
//        File key = new File(System.getProperty("user.dir"), "Key.yml");
//        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(key);
//        String qq = yamlConfiguration.getString("QQ");
//        String cdk = yamlConfiguration.getString("CDK");
//        try {
//            Advertisement advertisement = new Advertisement(qq, cdk);
//            advertisement.onSend();
//        } catch (Exception e) {
//            System.out.println("无法链接验证服务器");
//            Bukkit.shutdown();
//        }

    }

}
