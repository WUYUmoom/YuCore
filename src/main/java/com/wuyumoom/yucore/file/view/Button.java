package com.wuyumoom.yucore.file.view;

import com.wuyumoom.yucore.api.BukkitAPI;
import com.wuyumoom.yucore.api.ItemStackAPI;
import com.wuyumoom.yucore.api.pokemon.base.YuSprite;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Button {
    private final String buttonName;
    private final String name;
    private final int[] slot;
    private final List<String> lore;
    private final String id;
    private final ItemStack itemStack;
    private final Boolean enchantment;
    private final Boolean load;
    private final List<String> cmd;

    public String getId() {
        return id;
    }

    public Button(ConfigurationSection configurationSection){
        buttonName = configurationSection.getName();
        name = BukkitAPI.onReplace(configurationSection.getString("name"));
        slot = BukkitAPI.onSetInt(configurationSection.getString("slot"));
        lore = BukkitAPI.onReplace(configurationSection.getStringList("lore"));
        id = configurationSection.getString("id", "STONE");
        enchantment = configurationSection.getBoolean("enchantment");
        itemStack = createItemStack();
        load = configurationSection.getBoolean("load");
        cmd = configurationSection.getStringList("cmd");
    }

    @Deprecated
    public Button(String buttonName, String name, String slot, List<String> lore, String id, Boolean enchantment, Boolean load, @Nullable List<String> cmd) {
        this.buttonName = buttonName;
        this.name = BukkitAPI.onReplace(name);
        this.slot = BukkitAPI.onSetInt(slot);
        this.lore = BukkitAPI.onReplace(lore);
        this.id = id;
        this.enchantment = enchantment;
        this.itemStack = createItemStack();
        this.load = load;
        this.cmd = cmd;
    }

    private ItemStack createItemStack() {
        if (id.contains(":")){
            String[] split = id.split(":");
            if (split[0].equals("pokemon")) {
                try {
                    return ItemStackAPI.setNBT(ItemStackAPI.onSetItemMeta(YuSprite.getSprite(split[1]), name, lore,enchantment), "yubutton",buttonName );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return ItemStackAPI.setNBT(ItemStackAPI.onSetItemMeta(ItemStackAPI.onGetItemStack(Material.STONE), name, lore,enchantment), "yubutton",buttonName);
        }else {
            return ItemStackAPI.setNBT(ItemStackAPI.onSetItemMeta(ItemStackAPI.onGetItemStack(Objects.requireNonNullElse(Material.getMaterial(id), Material.STONE)), name, lore,enchantment), "yubutton",buttonName);
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

    public Boolean getLoad() {
        return load;
    }

    public List<String> getCmd() {
        return cmd;
    }

    public Boolean getEnchantment() {
        return enchantment;
    }

    public String getName() {
        return name;
    }

    public String getButtonName() {
        return buttonName;
    }
}