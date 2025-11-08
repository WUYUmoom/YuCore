package com.wuyumoom.yucore.view;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class AbstractUI implements InventoryHolder {
    private final String title;
    private Player player;
    private int size;
    private Inventory inventory;
    private Boolean isClick = true;

    public AbstractUI(String title, Player player, int size) {
        this.title = title;
        this.player = player;
        this.size = size;
        this.inventory = Bukkit.createInventory(this, this.size, this.title);
    }
    public void setClick(Boolean click) {
        isClick = click;
    }
    public Boolean getClick() {
        return isClick;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
    public abstract void draw();

    public abstract void openInventory(Player player);

    public abstract void closeInventory();

    public abstract void onClick(int clickSlot, InventoryClickEvent event);

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getTitle() {
        return title;
    }

    public Player getPlayer() {
        return player;
    }

    public int getSize() {
        return size;
    }
}

