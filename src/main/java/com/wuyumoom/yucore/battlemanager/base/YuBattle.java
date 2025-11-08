package com.wuyumoom.yucore.battlemanager.base;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.wuyumoom.yucore.battlemanager.rule.Rule;
import org.bukkit.entity.Player;

import java.util.List;

public interface YuBattle {

    PokemonBattle getPokemonBattle();
    Rule getRule();
    void spawn(Boolean isStart);

    void setWin(Player player);
    PokemonBattle pvp();
    void onCommand(String player, List<String> commands);
    void sendMessage(String message);

}
