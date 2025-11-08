package com.wuyumoom.yucore.api.pokemon.base;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.wuyumoom.yucore.api.BukkitAPI;
import com.wuyumoom.yucore.api.pokemon.PokemonAPI;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class YuMove {
    private static Map<String , Move> moves = new HashMap<>();
    public static Move getMove(String move) throws Exception {
        String lowerCase = move.toLowerCase(Locale.ENGLISH);
        if (BukkitAPI.isPureChinese(lowerCase)&&!YuMove.moves.isEmpty()) {
            return moves.get(lowerCase);
        } else {
            return Moves.INSTANCE.getByName(lowerCase).create();
        }
    }
    public static String getMoveSkills(Pokemon pokemon, int slots) {
        try {
            return PokemonAPI.onGetTranslatePath(pokemon.getMoveSet().getMoves().get(slots).getTemplate().getDisplayName());
        } catch (Exception e) {
            return "无";
        }
    }

    public static Map<String, Move> getMoves() {
        return moves;
    }
}
