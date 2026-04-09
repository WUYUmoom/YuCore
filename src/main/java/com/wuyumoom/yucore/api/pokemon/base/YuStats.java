package com.wuyumoom.yucore.api.pokemon.base;

import com.cobblemon.mod.common.api.pokemon.stats.Stat;

import java.util.HashMap;
import java.util.Map;

public class YuStats {
    private static Map<String, Stat> stats = new HashMap<>();

    public static Map<String, Stat> getStats() {
        return stats;
    }
}
