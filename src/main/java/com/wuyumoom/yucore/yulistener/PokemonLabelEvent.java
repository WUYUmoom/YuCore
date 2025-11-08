package com.wuyumoom.yucore.yulistener;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.api.events.CobblemonEvents;
import com.cobblemon.mod.common.api.events.battles.BattleFledEvent;
import com.cobblemon.mod.common.api.events.battles.BattleStartedPreEvent;
import com.cobblemon.mod.common.api.events.battles.BattleVictoryEvent;
import com.cobblemon.mod.common.api.events.pokeball.ThrownPokeballHitEvent;
import com.cobblemon.mod.common.api.events.pokemon.PokemonNicknamedEvent;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.battles.actor.PlayerBattleActor;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.wuyumoom.yucore.YuCore;
import com.wuyumoom.yucore.api.BukkitAPI;
import com.wuyumoom.yucore.api.pokemon.BattleCaptureFix;
import com.wuyumoom.yucore.api.pokemon.PokemonLabel;
import com.wuyumoom.yucore.battlemanager.BattleManager;
import kotlin.Unit;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.bukkit.Bukkit;

import java.util.Objects;

public class PokemonLabelEvent {
    public PokemonLabelEvent() {
        CobblemonEvents.BATTLE_STARTED_PRE.subscribe(Priority.HIGHEST, event -> {
            this.isYuBanBattle(event);
            return Unit.INSTANCE;
        });
        CobblemonEvents.THROWN_POKEBALL_HIT.subscribe(Priority.HIGHEST, event -> {
            this.isYuBanCapture(event);
            return Unit.INSTANCE;
        });
        CobblemonEvents.POKEMON_NICKNAMED.subscribe(Priority.HIGHEST, event -> {
            this.isYuBanNick(event);
            return Unit.INSTANCE;
        });
        CobblemonEvents.BATTLE_VICTORY.subscribe(Priority.HIGHEST, event -> {
            this.battleVictory(event);
            return Unit.INSTANCE;
        });
        CobblemonEvents.BATTLE_FLED.subscribe(Priority.HIGHEST, event -> {
            this.battleFledEvent(event);
            return Unit.INSTANCE;
        });
    }


    private void battleFledEvent(BattleFledEvent event) {
        if (event.getBattle().getPlayers().size() <= 1) return;
        if (BattleManager.getBattleManagerMap().containsKey(event.getBattle())) {
            ServerPlayerEntity entity = event.getPlayer().getEntity();
            if (entity == null) {
                return;
            }
            BattleManager battleManager = BattleManager.getBattleManagerMap().get(event.getBattle());
            battleManager.setWin(battleManager.getPlayer(Bukkit.getPlayer(event.getPlayer().getEntity().getUuid())));
        }
    }

    private void battleVictory(BattleVictoryEvent event) {
        if (event.getBattle().getPlayers().size() <= 1) return;
        if (BattleManager.getBattleManagerMap().containsKey(event.getBattle())) {
            for (BattleActor winner : event.getWinners()) {
                PlayerBattleActor playerActor = (PlayerBattleActor) winner;
                ServerPlayerEntity player = playerActor.getEntity();
                if (player != null) {
                    BattleManager.getBattleManagerMap().get(event.getBattle()).setWin(Objects.requireNonNull(Bukkit.getPlayer(player.getUuid())));
                }
            }
        }
    }

    private void isYuBanNick(PokemonNicknamedEvent event) {
        if (PokemonLabel.getInstance(event.getPokemon()).getLabelBoolean("YuBanNick")) {
            event.cancel();
        }
    }

    private void isYuBanCapture(ThrownPokeballHitEvent event) {
        Pokemon pokemon = event.getPokemon().getPokemon();
        PokemonLabel pokemonLabel = PokemonLabel.getInstance(pokemon);
        if (pokemonLabel.isLabel()) {
            return;
        }
        Entity owner = event.getPokeBall().getOwner();
        if (owner instanceof ServerPlayerEntity serverPlayerEntity){
            if (pokemonLabel.getLabelBoolean("YuBanCapture")) {
                BattleCaptureFix.onBattleCaptureFix(event.getPokeBall(), event.getPokemon());
                BukkitAPI.sendMessage(YuCore.getMessage().get("noCapture"), Bukkit.getPlayer(serverPlayerEntity.getUuid()));
                event.cancel();
                return;
            }
            String yuSpecifyCapture = pokemonLabel.getLabelContainsBoolean("YuSpecifyCapture");
            if (!yuSpecifyCapture.equals("no")) {
                if (!yuSpecifyCapture.equals(serverPlayerEntity.getName().getString())) {
                    BattleCaptureFix.onBattleCaptureFix(event.getPokeBall(), event.getPokemon());
                    event.cancel();
                    BukkitAPI.sendMessage(YuCore.getMessage().get("noCapture"), Bukkit.getPlayer(owner.getUuid()));
                }

            }
        }
    }

    private void isYuBanBattle(BattleStartedPreEvent event) {
        if (event.getBattle().getPlayers().size() > 1) {
            return;
        }
        ServerPlayerEntity first = event.getBattle().getPlayers().getFirst();
        for (BattleSide side : event.getBattle().getSides()) {
            for (BattleActor actor : side.getActors()) {
                for (BattlePokemon battlePokemon : actor.getPokemonList()) {
                    Pokemon originalPokemon = battlePokemon.getOriginalPokemon();
                    PokemonLabel pokemonLabel = PokemonLabel.getInstance(originalPokemon);
                    if (pokemonLabel.isLabel()) {
                        continue;
                    }
                    if (originalPokemon.getOwnerPlayer() != null) {
                        continue;
                    }
                    // 检查是否禁止战斗
                    if (pokemonLabel.getLabelBoolean("YuBanBattle")) {
                        BukkitAPI.sendMessage(YuCore.getMessage().get("noBattle"), Bukkit.getPlayer(first.getUuid()));
                        event.cancel();
                        return;
                    }
                    // 获取指定玩家名称
                    String specifiedPlayer = pokemonLabel.getLabelContainsBoolean("YuSpecifyBattle");
                    // 如果设置了指定玩家且当前玩家不是指定玩家，则取消对战
                    if (!specifiedPlayer.equals("no") && !first.getName().getString().equals(specifiedPlayer)) {
                        BukkitAPI.sendMessage(YuCore.getMessage().get("noBattle"), Bukkit.getPlayer(first.getUuid()));
                        event.cancel();
                        return;
                    }
                }
            }
        }
    }
}
