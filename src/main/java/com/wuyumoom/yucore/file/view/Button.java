package com.wuyumoom.yucore.file.view;

import com.wuyumoom.yucore.api.BukkitAPI;

import java.util.List;

public class Button {
    private final String buttonName;
    private final String name;
    private final int[] slot;
    private final List<String> lore;
    private final String id;

    public String getId() {
        return id;
    }

    public Button(String buttonName, String name, String slot, List<String> lore, String id) {
        this.buttonName = buttonName;
        this.name = BukkitAPI.onReplace(name);
        this.slot = BukkitAPI.onSetInt(slot);
        this.lore = BukkitAPI.onReplace(lore);
        this.id = id;
    }



    public List<String> getLore() {
        return lore;
    }

    public int[] getSlot() {
        return slot;
    }

    public String getName() {
        return name;
    }

    public String getButtonName() {
        return buttonName;
    }
}