package com.wuyumoom.yucore.api;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.wuyumoom.yucore.YuCore;
import com.wuyumoom.yucore.api.pokemon.PokemonAPI;
import com.wuyumoom.yucore.file.view.Button;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ItemStackAPI {
    public static ItemStack createItem(ConfigurationSection yamlConfiguration,boolean siNbt){
        String name = BukkitAPI.onReplace(yamlConfiguration.getString("name","名称配置错误"));
        List<String> lore = BukkitAPI.onReplace(yamlConfiguration.getStringList("lore"));
        Material material = Material.getMaterial(BukkitAPI.onReplace(yamlConfiguration.getString("id")));
        if (material != null){
            ItemStack itemStack = ItemStackAPI.onSetItemMeta(ItemStackAPI.onGetItemStack(material), name, lore);
            if (siNbt){
                return setNBT(itemStack, "yuitem", yamlConfiguration.getName());
            }
            return ItemStackAPI.onSetItemMeta(ItemStackAPI.onGetItemStack(material), name, lore);
        }
        return null;

    }
    @Deprecated
    public static ItemStack createItem(ConfigurationSection yamlConfiguration){
        String name = BukkitAPI.onReplace(yamlConfiguration.getString("name","名称配置错误"));
        List<String> lore = BukkitAPI.onReplace(yamlConfiguration.getStringList("lore"));
        Material material = Material.getMaterial(BukkitAPI.onReplace(yamlConfiguration.getString("id")));
        if (material != null){
            return ItemStackAPI.onSetItemMeta(ItemStackAPI.onGetItemStack(material), name, lore);
        }
        return null;

    }

    public static ItemStack onSetItemMeta(ItemStack itemStack,String name,List<String> lore){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack onSetItemMeta(ItemStack itemStack, Button button){
        ItemMeta itemMeta = itemStack.getItemMeta();
        String name = button.getName();
        List<String> lore = new ArrayList<>(button.getLore());
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack onSetItemMeta(ItemStack itemStack, Button button, Pokemon pokemon){
        ItemMeta itemMeta = itemStack.getItemMeta();
        String name = PokemonAPI.onReplace(button.getName(), pokemon);
        List<String> lore = Arrays.asList(PokemonAPI.onReplace(String.join("\n", button.getLore()),pokemon).split("\n"));
        itemMeta.setLore(lore);
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    public static ItemStack setNBT(ItemStack itemStack, String key, String value) {
        NBT.modify(itemStack, nbt -> {
            nbt.setString(key, value);
        });
        return itemStack;
    }
    public static Set<String> getKeyNBT(ItemStack itemStack) {
        AtomicReference<Set<String>> s = new AtomicReference<>(new HashSet<>());
        NBT.modify(itemStack, nbt -> {
            s.set(nbt.getKeys());
        });
        return s.get();
    }

    public static String getNBT(ItemStack itemStack, String key) {
        AtomicReference<String> s = new AtomicReference<>();
        NBT.modify(itemStack, nbt -> {
            s.set(nbt.getString(key));
        });
        return s.get();
    }
    public static ItemStack onGetItemStack(Material material){
        if (material == null) {
            YuCore.getInstance().getLogger().severe( "material id 字段无效: "+material);
            return null;
        } else {
            return new ItemStack(material);
        }
    }

}
