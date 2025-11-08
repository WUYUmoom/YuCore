package com.wuyumoom.yucore.api.pokemon;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.entity.pokemon.PokemonServerDelegate;

public class BattleCaptureFix {
    public static void onBattleCaptureFix(EmptyPokeBallEntity pokeBall,PokemonEntity pokemonEntity)  {
        if (pokemonEntity.getDelegate() instanceof PokemonServerDelegate pokemonServerDelegate) {
            PokemonBattle battle = pokemonServerDelegate.getBattle();
            if (battle != null){
                battle.getCaptureActions().stream()
                        .filter(action -> action.getPokeBallEntity() == pokeBall)
                        .findFirst().ifPresent(battle::finishCaptureAction);
            }
        }
    }
}
