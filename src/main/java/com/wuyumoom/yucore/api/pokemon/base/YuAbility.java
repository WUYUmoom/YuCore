package com.wuyumoom.yucore.api.pokemon.base;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.abilities.Abilities;
import com.cobblemon.mod.common.api.abilities.Ability;
import com.wuyumoom.yucore.api.BukkitAPI;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class YuAbility {
    //所以特性
    private static Map<String , Ability> abilityMap = new HashMap<>();
    public static Ability getAbility(String ability) throws Exception {
        String lowerCase = ability.toLowerCase(Locale.ENGLISH);
        if (BukkitAPI.isPureChinese(lowerCase)&&!abilityMap.isEmpty()) {
            return abilityMap.get(lowerCase);
        } else {
            return  Abilities.INSTANCE.get(lowerCase).create(true, Priority.LOWEST);
        }
    }

    public static Map<String, Ability> getAbilityMap() {
        return abilityMap;
    }
}
