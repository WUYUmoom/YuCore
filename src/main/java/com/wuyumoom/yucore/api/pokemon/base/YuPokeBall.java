package com.wuyumoom.yucore.api.pokemon.base;

import com.cobblemon.mod.common.api.pokeball.PokeBalls;
import com.cobblemon.mod.common.pokeball.PokeBall;
import com.wuyumoom.yucore.api.BukkitAPI;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class YuPokeBall {
    private static Map<String, PokeBall> zhPokeBall = new HashMap<>();
    public static PokeBall getPokeBall(String pokeBall) throws Exception {
        String lowerCase = pokeBall.toLowerCase(Locale.ENGLISH);
        if (BukkitAPI.isPureChinese(lowerCase)&&!zhPokeBall.isEmpty()) {
            return zhPokeBall.get(lowerCase);
        } else {
            return PokeBalls.getPokeBall(ResourceLocation.parse("cobblemon:"+lowerCase));
        }
    }

    public static Map<String, PokeBall> getPokeBall() {
        return zhPokeBall;
    }
}
