package com.wuyumoom.yucore.api.pokemon.base;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.types.ElementalType;
import com.cobblemon.mod.common.api.types.ElementalTypes;
import com.cobblemon.mod.common.pokemon.Species;
import com.wuyumoom.yucore.YuCore;
import com.wuyumoom.yucore.api.BukkitAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class YuSpecies {
    private static final Map<String, Species> species = new HashMap<>();
    private static final Map<String, List<Species>> labelSpecies = new HashMap<>();
    private static Map<ElementalType, List<Species> > typeSpecies = new HashMap<>();


    public static Map<String, Species> getSpecies() {
        return species;
    }
    public static Map<String, List<Species>> getLabelSpecies() {
        return labelSpecies;
    }

    public static void addTypeSpecies(ElementalType type, Species species){
        addSpeciesList(type, species, typeSpecies);
    }
    public static Map<ElementalType, List<Species> > getAllTypeSpecies() {
        return typeSpecies;
    }
    public static List<Species> getTypeSpecies(ElementalType type) {
        return typeSpecies.get(type);
    }
    public static List<Species> getTypeSpecies(String string) {
        try {
            return typeSpecies.get(ElementalTypes.get(string));
        } catch (Exception e) {
            YuCore.getInstance().getLogger().warning("未找到对应宝可梦种类: " + string);
            return null;
        }
    }

    private static void addSpeciesList(ElementalType type, Species species, Map<ElementalType, List<Species>> typeSpecies) {
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
    private static void addSpeciesList(String label, Species species, Map<String, List<Species>> labelSpecies) {
        List<Species> speciesList = labelSpecies.get(label);
        if (speciesList == null){
            List<Species> newSpeciesList = new ArrayList<>();
            newSpeciesList.add(species);
            labelSpecies.put(label, newSpeciesList);
        }else {
            speciesList.add(species);
            labelSpecies.put(label, speciesList);
        }
    }

    public static void addLabelSpecies(String label, Species species){
        addSpeciesList(label, species, labelSpecies);
    }


    private static final Random random = new Random();
    // 随机获取一个Species
    public static Species getRandomSpecies() {
        List<Species> speciesList = new ArrayList<>(species.values());
        return speciesList.get(random.nextInt(speciesList.size()));
    }

    public static Species getSpecies(String name) throws Exception {
        if (name.contains("随机")){
            if (name.contains("@")){
                List<Species> speciesList = new ArrayList<>(species.values());
                String[] strings = BukkitAPI.onSetString(name, "@");
                for (String string : strings) {
                    if (string.contains("!")){
                        List<Species> labelSpecies1 = labelSpecies.get(string.replace("!", ""));
                        speciesList.removeAll(labelSpecies1);
                    }else {
                        // 处理不包含"!"的情况
                        List<Species> includeSpecies = labelSpecies.get(string);
                        if (includeSpecies != null) {
                            speciesList.retainAll(includeSpecies);  // 只保留包含在标签中的物种
                        }
                    }
                }
                return speciesList.get(random.nextInt(speciesList.size()));
            }else {
                return getRandomSpecies();
            }
        }
        String lowerCase = name.toLowerCase(Locale.ENGLISH);
        Species species;// 对于非中文名称同样处理
        if (BukkitAPI.isPureChinese(lowerCase) && !YuSpecies.species.isEmpty()) {
            species = YuSpecies.species.get(lowerCase);
        } else {
            species = PokemonSpecies.getByName(lowerCase);
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
            List<Species> speciesList = getLabelSpecies().get(label);
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
                    Species cobblemon = PokemonSpecies.getByPokedexNumber(i, "cobblemon");
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
