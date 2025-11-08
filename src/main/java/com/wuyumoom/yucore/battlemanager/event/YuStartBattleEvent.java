package com.wuyumoom.yucore.battlemanager.event;

import com.wuyumoom.yucore.battlemanager.BattleManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class YuStartBattleEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final BattleManager battleManager;
    public YuStartBattleEvent(BattleManager battleManager ) {
        this.battleManager = battleManager;
    }

    public BattleManager getBattleManager() {
        return battleManager;
    }


    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
