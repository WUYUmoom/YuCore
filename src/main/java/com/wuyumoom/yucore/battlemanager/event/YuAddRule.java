package com.wuyumoom.yucore.battlemanager.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class YuAddRule extends Event {
    private static final HandlerList handlers = new HandlerList();
    public YuAddRule() {}

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
