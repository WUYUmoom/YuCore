package com.wuyumoom.yucore.lang;

import com.cobblemon.mod.common.api.Priority;
import com.cobblemon.mod.common.api.abilities.Abilities;
import com.cobblemon.mod.common.api.abilities.AbilityTemplate;
import com.cobblemon.mod.common.api.moves.MoveTemplate;
import com.cobblemon.mod.common.api.moves.Moves;
import com.cobblemon.mod.common.api.pokeball.PokeBalls;
import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.pokeball.PokeBall;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Nature;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.cobblemon.mod.common.util.MiscUtilsKt;
import com.wuyumoom.yucore.YuCore;
import com.wuyumoom.yucore.api.pokemon.PokemonAPI;
import com.wuyumoom.yucore.api.pokemon.base.*;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TextContent;
import net.minecraft.text.TranslatableTextContent;
import org.bukkit.Bukkit;

public class LangLoad {
    public static void loadGender() {
        YuGender.getGender().put("雄", Gender.MALE);
        YuGender.getGender().put("雌", Gender.FEMALE);
        YuGender.getGender().put("无", Gender.GENDERLESS);
    }
    public static void loadStat(){
        for (Stat onGetIV : PokemonAPI.onGetIVS()) {
            if (onGetIV.getShowdownId().equals("hp")) {
                YuStats.getStats().put("血量", onGetIV);
            }
            if (onGetIV.getShowdownId().equals("atk")) {
                YuStats.getStats().put("攻击", onGetIV);
            }
            if (onGetIV.getShowdownId().equals("def")) {
                YuStats.getStats().put("防御", onGetIV);
            }
            if (onGetIV.getShowdownId().equals("spa")) {
                YuStats.getStats().put("特攻", onGetIV);
            }
            if (onGetIV.getShowdownId().equals("spd")) {
                YuStats.getStats().put("特防", onGetIV);
            }
            if (onGetIV.getShowdownId().equals("spe")) {
                YuStats.getStats().put("速度", onGetIV);
            }
        }
    }

    public static void loadMove() {
        for (MoveTemplate moveTemplate : Moves.INSTANCE.all()) {
            TextContent content = moveTemplate.getDisplayName().getContent();
            if (content instanceof TranslatableTextContent translatableContent) {
                if (YuCore.isIsTranslatePath()){
                    String s = YuCore.getCobblemon().getZh().get(translatableContent.getKey());
                    if (s == null) {
                        s = translatableContent.getKey();
                        System.out.println("技能"+s);
                        Bukkit.getConsoleSender().sendMessage(moveTemplate.getName() + "技能:未找到翻译目录,请在assets/cobblemon/lang/zh_cn.json里添加翻译");
                    }
                    YuMove.getMoves().put(s, moveTemplate.create());
                }else {
                    YuMove.getMoves().put(moveTemplate.getName(), moveTemplate.create());
                }
            }
        }
    }
    public static void loadAbilities() {
        for (AbilityTemplate abilityTemplate : Abilities.INSTANCE.all()) {
            if (YuCore.isIsTranslatePath()){
                String s = YuCore.getCobblemon().getZh().get(abilityTemplate.getDisplayName());
                if (s == null){
                    s = abilityTemplate.getDisplayName();
                    System.out.println("特性"+s);
                    Bukkit.getConsoleSender().sendMessage(abilityTemplate.getName() + "特性:未找到翻译目录,请在assets/cobblemon/lang/zh_cn.json里添加翻译");
                }
                YuAbility.getAbilityMap().put(s, abilityTemplate.create(true, Priority.LOWEST));
            }else {
                YuAbility.getAbilityMap().put(MiscUtilsKt.asTranslated(abilityTemplate.getDisplayName()).getString(), abilityTemplate.create(true, Priority.LOWEST));
            }
        }
    }
    public static void loadPokeBall() {
        for (PokeBall pokeBall : PokeBalls.INSTANCE.all()) {
            ItemStack stack = pokeBall.stack(1);
            if (YuCore.isIsTranslatePath()){
                String s = YuCore.getCobblemon().getZh().get(stack.getTranslationKey());
                if (s == null){
                    s = stack.getTranslationKey();
                    System.out.println("球"+s);
                    Bukkit.getConsoleSender().sendMessage(stack.getName().getString() + "球:未找到翻译目录,请在assets/cobblemon/lang/zh_cn.json里添加翻译");
                }
                YuPokeBall.getPokeBall().put(s, pokeBall);
            }else {
                YuPokeBall.getPokeBall().put(stack.getName().getString(), pokeBall);
            }
        }
    }

    public static void loadNature(){
        for (Nature nature : Natures.INSTANCE.all()) {
            if (YuCore.isIsTranslatePath()){
                String s = YuCore.getCobblemon().getZh().get(nature.getDisplayName());
                if (s == null){
                    s = nature.getDisplayName();
                    System.out.println("性格"+s);
                    Bukkit.getConsoleSender().sendMessage(nature.getDisplayName() + "性格:未找到翻译目录,请在assets/cobblemon/lang/zh_cn.json里添加翻译");
                }
                YuNature.getNature().put(s, nature);
            }else {
                YuNature.getNature().put(MiscUtilsKt.asTranslated(nature.getDisplayName()).getString(), nature);
            }
        }
    }
    public static void loadSpecies() {
        for (Species species : PokemonSpecies.INSTANCE.getSpecies()) {
            TextContent content = species.getTranslatedName().getContent();
            if (content instanceof TranslatableTextContent translatableContent) {
                if (YuCore.isIsTranslatePath()){
                    String s = YuCore.getCobblemon().getZh().get(translatableContent.getKey());
                    if (s == null) {
                        s = translatableContent.getKey();
                        System.out.println("精灵"+s);
                        Bukkit.getConsoleSender().sendMessage(species.getTranslatedName().getString() + "精灵:未找到翻译目录,请在assets/cobblemon/lang/zh_cn.json里添加翻译");
                    }
                    YuSpecies.getSpecies().put(s, species);
                }else {
                    YuSpecies.getSpecies().put(species.getTranslatedName().getString(), species);
                }
                for (String label : species.getLabels()) {
                    YuSpecies.addLabelSpecies(label, species);
                }
                YuSpecies.addTypeSpecies(species.getPrimaryType().getDisplayName().getString(),species);
            }
        }
    }
}
