package com.wuyumoom.yucore.api.pokemon.base;

import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.wuyumoom.yucore.api.BukkitAPI;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class YuGender {
    private static Map<String,Gender> gender = new HashMap<>();


    public static String onGetGender(Pokemon pokemon) {
        if (pokemon.getGender() == Gender.MALE) {
            return "雄";
        } else if (pokemon.getGender() == Gender.FEMALE) {
            return "雌";
        }
        return "未知";
    }
    public static Gender getGender(String gender) throws Exception {
        String lowerCase = gender.toLowerCase(Locale.ENGLISH);
        if (BukkitAPI.isPureChinese(lowerCase)){
            return YuGender.gender.get(lowerCase);
        }else {
            return Gender.valueOf(lowerCase);
        }

    }

    public static Map<String, Gender> getGender() {
        return gender;
    }
}
