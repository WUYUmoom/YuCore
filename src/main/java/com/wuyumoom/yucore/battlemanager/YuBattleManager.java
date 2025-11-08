package com.wuyumoom.yucore.battlemanager;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import com.cobblemon.mod.common.battles.BattleSide;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import com.wuyumoom.yucore.battlemanager.rule.Rule;
import net.minecraft.server.network.ServerPlayerEntity;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class YuBattleManager  {
    private PokemonBattle pokemonBattle;
    private Rule rule;
    private Map<Player, Location> playerLocationMap = new HashMap<>();


    public void setWin(Player player) {
        ServerPlayerEntity serverPlayerEntity = PlayerExtensionsKt.getPlayer(player.getUniqueId());
        if (serverPlayerEntity == null){
            return;
        }
        List<ServerPlayerEntity> team = getTeam(serverPlayerEntity);
        for (ServerPlayerEntity serverPlayer : team) {

        }
    }
    public void setLosers(Player player){
        ServerPlayerEntity serverPlayerEntity = PlayerExtensionsKt.getPlayer(player.getUniqueId());
        if (serverPlayerEntity == null){
            return;
        }
        List<ServerPlayerEntity> opponentSide = getOpponentSide(serverPlayerEntity);
        for (ServerPlayerEntity serverPlayer : opponentSide) {

        }
    }


    public List<ServerPlayerEntity> getTeam(ServerPlayerEntity  player){
        BattleActor targetActor = pokemonBattle.getActor(player);
        if (targetActor == null) {
            return new ArrayList<>();
        }
        // 获取目标玩家所在的一边
        //BattleSide playerSide = targetActor.getSide();
        // 获取对手一边
        BattleSide opponentSide = targetActor.getSide().getOppositeSide();
        return getListPlayer(opponentSide);
    }
    public List<ServerPlayerEntity> getOpponentSide(ServerPlayerEntity  player){
        BattleActor targetActor = pokemonBattle.getActor(player);
        if (targetActor == null) {
            return new ArrayList<>();
        }
        BattleSide opponentSide = targetActor.getSide().getOppositeSide();
        return getListPlayer(opponentSide);
    }

    private List<ServerPlayerEntity> getListPlayer(BattleSide playerSide){
        return Arrays.stream(playerSide.getActors())
                .filter(actor -> actor.getType() == ActorType.PLAYER)
                .flatMap(actor -> StreamSupport.stream(actor.getPlayerUUIDs().spliterator(), false))
                .map(PlayerExtensionsKt::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


//    public void removePlayerFromMultiBattle(UUID uuid){
//        ServerPlayerEntity serverPlayerEntity = PlayerExtensionsKt.getPlayer(uuid);
//        if (serverPlayerEntity == null){
//            return;
//        }
//        BattleActor actor = pokemonBattle.getActor(serverPlayerEntity);
//        if (!pokemonBattle.getFormat().getBattleType().getName().equals("multi")) {
//            forcePlayerForfeit(actor);
//            return;
//        }
//    }
//
//
//    private void forcePlayerForfeit(BattleActor actor) {
//        pokemonBattle.dispatchGo(() -> {
//            // 广播投降消息
//            String forfeitPlayerName = actor.getName().getString();
//            pokemonBattle.broadcastChatMessage(Text.of(forfeitPlayerName+" 投降了"));
//
//            // 使用现有的投降机制
//            pokemonBattle.writeShowdownAction(">forcelose " + actor.showdownId);
//            return Unit.INSTANCE;
//        });
//    }
}
