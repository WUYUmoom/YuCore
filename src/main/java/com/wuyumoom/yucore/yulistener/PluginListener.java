package com.wuyumoom.yucore.yulistener;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleRegistry;
import com.wuyumoom.yucore.YuCore;
import com.wuyumoom.yucore.battlemanager.BattleManager;
import com.wuyumoom.yucore.task.Advertisement;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerLoadEvent;

import java.io.File;

public class PluginListener implements Listener {

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        Advertisement.startTimer(YuCore.getInstance());
        File key = new File(System.getProperty("user.dir"), "Key.yml");
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(key);
        String qq = yamlConfiguration.getString("QQ");
        String cdk = yamlConfiguration.getString("CDK");
        try {
            Advertisement advertisement = new Advertisement(qq, cdk);
            advertisement.onSend();
        } catch (Exception e) {
            System.out.println("无法链接验证服务器");
            Bukkit.shutdown();
        }

    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        PokemonBattle battleByParticipatingPlayerId = BattleRegistry.INSTANCE.getBattleByParticipatingPlayerId(event.getPlayer().getUniqueId());
        if (battleByParticipatingPlayerId != null) {
            if (BattleManager.getBattleManagerMap().containsKey(battleByParticipatingPlayerId)){
                BattleManager battleManager = BattleManager.getBattleManagerMap().get(battleByParticipatingPlayerId);
                battleManager.setWin(battleManager.getPlayer(event.getPlayer()));
            }
        }
    }

}
