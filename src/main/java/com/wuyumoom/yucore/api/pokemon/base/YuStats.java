package com.wuyumoom.yucore.api.pokemon.base;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.abilities.Abilities;
import com.cobblemon.mod.common.api.abilities.Ability;
import com.cobblemon.mod.common.api.pokeball.PokeBalls;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.pokeball.PokeBall;
import com.wuyumoom.yucore.api.BukkitAPI;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class YuStats {
    private static Map<String, Stat> stats = new HashMap<>();

    public static Map<String, Stat> getStats() {
        return stats;
    }
}
