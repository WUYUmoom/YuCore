package com.wuyumoom.yucore.api.pokemon.base;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.wuyumoom.yucore.YuCore;
import com.wuyumoom.yucore.api.BukkitAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class YuSpecies {
    private static final Map<String, Species> species = new HashMap<>();
    private static final Map<String, List<Species>> labelSpecies = new HashMap<>();
    private static Map<String, List<Species> > typeSpecies = new HashMap<>();


    public static Map<String, Species> getSpecies() {
        return species;
    }
    public static Map<String, List<Species>> getLabelSpecies() {
        return labelSpecies;
    }

    public static void addTypeSpecies(String type, Species species){
        addSpeciesList(type, species, typeSpecies);
    }

    private static void addSpeciesList(String type, Species species, Map<String, List<Species>> typeSpecies) {
        List<Species> speciesList = typeSpecies.get(type);
        if (speciesList == null){
            List<Species> newSpeciesList = new ArrayList<>();
            newSpeciesList.add(species);
            typeSpecies.put(type, newSpeciesList);
        }else {
            speciesList.add(species);
            typeSpecies.put(type, speciesList);
        }
    }

    public static void addLabelSpecies(String label, Species species){
        addSpeciesList(label, species, labelSpecies);
    }

    public static Species getSpecies(String name) throws Exception {
        String lowerCase = name.toLowerCase(Locale.ENGLISH);
        Species species;// 对于非中文名称同样处理
        if (BukkitAPI.isPureChinese(lowerCase) && !YuSpecies.species.isEmpty()) {
            species = YuSpecies.species.get(lowerCase);
        } else {
            species = PokemonSpecies.INSTANCE.getByName(lowerCase);
        }
        if (species == null) {
            throw new Exception("未找到对应宝可梦种类: " + name); // 抛出异常，提示未找到对应宝可梦
        }
        return species;
    }
    public static List<Species> loadSpecies(ConfigurationSection configurationSection) {
        List<Species> species = new ArrayList<>();
        String labelString = configurationSection.getString("label");
        if (labelString != null) {
            loadLabels(labelString, species);
        }
        String dex = configurationSection.getString("speciesDex");
        if (dex != null&&!dex.isEmpty()) {
            String[] strings = BukkitAPI.onSetString(dex, ",");
            if (strings != null){
                loadSpeciesDex(strings, species);
            }
        }
        List<String> stringList = configurationSection.getStringList("species");
        for (String s : stringList) {
            try {
                species.add(YuSpecies.getSpecies(s));
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("⚠️ 未知的 Pokemon: " + s);
            }
        }
        return species;
    }

    private static void loadLabels(String labelString, List<Species> species) {
        String[] labels = BukkitAPI.onSetString(labelString, ",");
        for (String label : labels) {
            List<Species> speciesList = YuSpecies.getLabelSpecies().get(label);
            if (speciesList != null && !speciesList.isEmpty()) {
                species.addAll(speciesList);
            }
        }
    }

    public static void loadSpeciesDex(String[] strings, List<Species> species) {
        for (String s : strings) {
            if (s.isEmpty()){
                continue;
            }
            String[] sp = BukkitAPI.onSetString(s, "@");
            if (sp.length < 2) {
                YuCore.getInstance().getLogger().warning("跳过格式不正确的条目: " + s);
                continue; // 跳过格式不正确的条目
            }
            try {
                int start = Integer.parseInt(sp[0]);
                int end = Integer.parseInt(sp[1]);
                for (int i = start; i <= end; i++) {
                    Species cobblemon = PokemonSpecies.INSTANCE.getByPokedexNumber(i, "cobblemon");
                    if (cobblemon == null){
                        System.out.println("未知的精灵编号: " + i);
                    }
                    species.add(cobblemon);
                }
            } catch (NumberFormatException e) {
                YuCore.getInstance().getLogger().warning("跳过格式不正确的条目: " + s);
            }
        }
    }
}
