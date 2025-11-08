package com.wuyumoom.yucore.battlemanager.event;

import com.wuyumoom.yucore.battlemanager.BattleManager;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class YuBattleWinners extends Event implements Cancellable {
    public YuBattleWinners(BattleManager battleManager, String Winners, String Losers) {
        this.battleManager = battleManager;
        this.Winners = Winners;
        this.Losers = Losers;
    }
    private final String Losers;
    private final String Winners;

    public String getWinners() {
        return Winners;
    }


    public String getLosers() {
        return Losers;
    }

    private final BattleManager battleManager;
    private boolean cancelled = false;

    public BattleManager getBattleManager() {
        return battleManager;
    }
    private static final HandlerList handlers = new HandlerList();
    public static HandlerList getHandlerList() {
        return handlers;
    }
    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}

