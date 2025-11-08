package com.wuyumoom.yucore.api.pokemon.base;

import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.item.PokemonItem;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.wuyumoom.yucore.api.ItemStackAPI;
import com.wuyumoom.yucore.api.pokemon.PokemonAPI;
import com.wuyumoom.yucore.file.view.Button;
import net.minecraft.nbt.NbtCompound;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class YuSprite {
    public static ItemStack onSetPokemonItem(ItemStack itemStack, Pokemon pokemon) {
        NbtCompound nbtTagCompound = new NbtCompound();
        nbtTagCompound = pokemon.saveToNBT(pokemon.getOwnerPlayer().getRegistryManager(), nbtTagCompound);
        ItemStackAPI.setNBT(itemStack, "pokemonNBT", nbtTagCompound.toString());
        ItemStackAPI.setNBT(itemStack, "UUID", pokemon.getUuid().toString());
        return itemStack;
    }
    public static Map<Integer, ItemStack> getPhoto(PlayerPartyStore playerPartyStorage, Button button, Boolean nbt) {
        Map<Integer, ItemStack> itemStack = new HashMap<>();
        Map<Integer, Pokemon> teamPokemon = PokemonAPI.getTeamPokemon(playerPartyStorage);
        for (int i = 0; i < teamPokemon.size(); i++) {
            if (teamPokemon.get(i) != null) {
                if (nbt) {
                    itemStack.put(i, onSetPokemonItem(ItemStackAPI.onSetItemMeta(getSpriteItem(teamPokemon.get(i)), button, teamPokemon.get(i)), teamPokemon.get(i)));
                } else {
                    itemStack.put(i, ItemStackAPI.onSetItemMeta(getSpriteItem(teamPokemon.get(i)), button, teamPokemon.get(i)));
                }
            } else {
                itemStack.put(i, null);
            }
        }
        return itemStack;
    }
    public static ItemStack getSpriteItem(Pokemon pokemon) {
        return CraftItemStack.asBukkitCopy((net.minecraft.world.item.ItemStack) (Object) PokemonItem.from(pokemon));
    }
    public static ItemStack getSprite(String name) throws Exception {
        return getSpriteItem(YuSpecies.getSpecies(name).create(1));
    }
}
