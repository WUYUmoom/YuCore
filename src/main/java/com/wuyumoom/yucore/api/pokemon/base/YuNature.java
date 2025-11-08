package com.wuyumoom.yucore.api.pokemon.base;

import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.pokemon.Nature;
import com.wuyumoom.yucore.api.BukkitAPI;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class YuNature {
    private static Map<String, Nature> nature = new HashMap<>();
    public static Nature getNature(String nature) throws Exception {
        String lowerCase = nature.toLowerCase(Locale.ENGLISH);
        if (BukkitAPI.isPureChinese(lowerCase)&&!YuNature.nature.isEmpty()) {
            return YuNature.nature.get(lowerCase);
        } else {
            return Natures.INSTANCE.getNature(lowerCase);
        }
    }

    public static Map<String, Nature> getNature() {
        return nature;
    }
}
