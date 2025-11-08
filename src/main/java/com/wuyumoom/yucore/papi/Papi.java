package com.wuyumoom.yucore.papi;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import com.wuyumoom.yucore.api.BukkitAPI;
import com.wuyumoom.yucore.api.pokemon.PokemonAPI;
import com.wuyumoom.yucore.api.pokemon.PokemonLabel;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.minecraft.server.network.ServerPlayerEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Papi extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "yupokelabel";
    }

    @Override
    public @NotNull String getAuthor() {
        return "无语";
    }

    @Override
    public @NotNull String getVersion() {
        return  "1.0";
    }
    //yupokelabel_位置_标签  返回yes或者no
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (player == null) {
            return "未知玩家";
        }
        String[] strings = BukkitAPI.onSetString(params, "_");
        if (strings.length == 2){
            try {
                int i = Integer.parseInt(strings[0]);
                if (i >= 0 && i < 6) {
                    ServerPlayerEntity serverPlayer = PlayerExtensionsKt.getPlayer(player.getUniqueId());
                    if (serverPlayer == null){
                        return "未找到玩家";
                    }
                    Map<Integer, Pokemon> teamPokemon = PokemonAPI.getTeamPokemon(PlayerExtensionsKt.party(serverPlayer));
                    Pokemon pokemon = teamPokemon.get(i);
                    if (pokemon != null) {
                        if (PokemonLabel.getInstance(pokemon).getLabelBoolean(strings[1])) {
                            return "yes";
                        }else {
                            return "no";
                        }
                    }else {
                        return "该位置没有宝可梦";
                    }
                } else {
                    return "位置错误请小于6";
                }
            } catch (Exception e) {
                return "位置错误请输入数字";
            }
        }else {
            return "变量输出错误请检查";
        }
    }
}
