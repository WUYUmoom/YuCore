package com.wuyumoom.yucore.file.view;

import com.wuyumoom.yucore.api.BukkitAPI;
import com.wuyumoom.yucore.api.ItemStackAPI;
import com.wuyumoom.yucore.api.pokemon.base.YuSprite;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class Button {
    private final String buttonName;
    private final String name;
    private final int[] slot;
    private final List<String> lore;
    private final String id;
    private final ItemStack itemStack;

    public String getId() {
        return id;
    }

    public Button(String buttonName, String name, String slot, List<String> lore, String id) {
        this.buttonName = buttonName;
        this.name = BukkitAPI.onReplace(name);
        this.slot = BukkitAPI.onSetInt(slot);
        this.lore = BukkitAPI.onReplace(lore);
        this.id = id;
        this.itemStack = createItemStack();

    }

    private ItemStack createItemStack() {
        if (id.contains(":")){
            String[] split = id.split(":");
            if (split[0].equals("pokemon")) {
                try {
                    return ItemStackAPI.setNBT(ItemStackAPI.onSetItemMeta(YuSprite.getSprite(split[1]), name, lore), "yubutton",buttonName );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return ItemStackAPI.setNBT(ItemStackAPI.onSetItemMeta(ItemStackAPI.onGetItemStack(Material.STONE), name, lore), "yubutton",buttonName);
        }else {
            return ItemStackAPI.setNBT(ItemStackAPI.onSetItemMeta(ItemStackAPI.onGetItemStack(Objects.requireNonNullElse(Material.getMaterial(id), Material.STONE)), name, lore), "yubutton",buttonName);
        }
    }

    public ItemStack getItemStack() {
        return itemStack;
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