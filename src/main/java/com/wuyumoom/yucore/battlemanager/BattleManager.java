package com.wuyumoom.yucore.battlemanager;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleBuilder;
import com.cobblemon.mod.common.battles.BattleStartResult;
import com.cobblemon.mod.common.battles.SuccessfulBattleStart;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import com.wuyumoom.yucore.YuCore;
import com.wuyumoom.yucore.api.BukkitAPI;
import com.wuyumoom.yucore.battlemanager.event.YuBattleWinners;
import com.wuyumoom.yucore.battlemanager.event.YuStartBattleEvent;
import com.wuyumoom.yucore.battlemanager.rule.Rule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BattleManager {
    private static Map<PokemonBattle, BattleManager> BattleManagerMap = new java.util.HashMap<>();
    private Player player1;
    private Player player2;
    private Location locationPlayer1;
    private Location locationPlayer2;
    private Rule rule;
    //开战时提示倒计时
    private int second;
    private PokemonBattle pokemonBattle;
    private Boolean inEnd = false;

    public BattleManager(Player player1, Player player2, Rule rule, Location locationPlayer1, Location locationPlayer2) {
        this.player1 = player1;
        this.player2 = player2;
        this.rule = rule;
        this.second = rule.getMatching_Second();
        this.locationPlayer1 = locationPlayer1;
        this.locationPlayer2 = locationPlayer2;
        spawn(true);
    }


    public void spawn(Boolean isStart) {
        if (isStart) {
            Bukkit.getScheduler().runTask(YuCore.getInstance(), () -> {
                player1.teleport(getRandomNearbyLocation(locationPlayer2));
            });
        } else {
            Bukkit.getScheduler().runTask(YuCore.getInstance(), () -> {
                player1.teleport(locationPlayer1);
            });
        }
    }
    private Location getRandomNearbyLocation(Location center) {
        double x = center.getX() + (Math.random() * 2 * 5 - 5);
        double z = center.getZ() + (Math.random() * 2 * 5 - 5);
        return new Location(center.getWorld(), x, center.getY(), z);
    }

    public PokemonBattle pvp() {
        BattleStartResult result = BattleBuilder.INSTANCE.pvp1v1(
                Objects.requireNonNull(PlayerExtensionsKt.getPlayer(player1.getUniqueId())),
                Objects.requireNonNull(PlayerExtensionsKt.getPlayer(player2.getUniqueId())),
                null, null,
                rule.getBattleFormat(),
                true,
                rule.isFullHeal());
        if (result instanceof SuccessfulBattleStart successfulBattleStart) {
            PokemonBattle battle = successfulBattleStart.getBattle();
            battle.getActors().forEach(actor -> {
            });
            BattleManagerMap.put(battle, this);
            pokemonBattle = battle;
            Bukkit.getPluginManager().callEvent(new YuStartBattleEvent(this));
            return battle;
        } else {
            return null;
        }
    }


    public void setWin(Player player) {
        setInEnd(true);
        YuBattleWinners yuBattleWinners = new YuBattleWinners(this, player.getName(), getPlayer(player).getName());
        BattleManagerMap.remove(pokemonBattle);
        Bukkit.getPluginManager().callEvent(yuBattleWinners);
        if (yuBattleWinners.isCancelled()) {
            return;
        }
        spawn(false);
        onCommand(player.getName(), rule.getWinCommands());
        onCommand(getPlayer(player).getName(), rule.getLoseCommands());
    }

    public Player getPlayer(Player player) {
        if (player == player1) {
            return player2;
        } else {
            return player1;
        }
    }

    private void onCommand(String player, List<String> commands) {
        for (String command : commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player));
        }
    }


    public void sendMessage(String message) {
        BukkitAPI.sendMessage(message.replace("%second%", String.valueOf(second)), player1);
        BukkitAPI.sendMessage(message.replace("%second%", String.valueOf(second)), player2);
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getSecond() {
        return second;
    }


    public Rule getRule() {
        return rule;
    }

    public static Map<PokemonBattle, BattleManager> getBattleManagerMap() {
        return BattleManagerMap;
    }

    public Boolean getInEnd() {
        return inEnd;
    }

    public void setInEnd(Boolean inEnd) {
        this.inEnd = inEnd;
    }

    public void setSecond(int i) {
        second = i;
    }

    public PokemonBattle getPokemonBattle() {
        return pokemonBattle;
    }
}

