package com.wuyumoom.yucore.api.pokemon;

import com.cobblemon.mod.common.pokemon.Pokemon;
import com.wuyumoom.yucore.api.BukkitAPI;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PokemonLabel {

    private PokemonLabel(Pokemon pokemon) {
        this.nbt = pokemon.getPersistentData();
        this.listLabel = getListLabel();
        this.pokemon = pokemon;
    }

    private final NbtCompound nbt;
    private Set<String> listLabel = new HashSet<>();
    private Pokemon pokemon;

    public static PokemonLabel getInstance(Pokemon pokemon) {
        return new PokemonLabel(pokemon);
    }

    /**
     * YuBanBattle 标签禁止战斗标签
     * YuBanCapture 禁止捕捉标签
     * YuBanNick 禁止修改昵称标签
     * YuSpecifyBattle@玩家 指定玩家对战标签
     * YuSpecifyCapture@玩家 指定玩家捕捉标签
     */

    public void saveLabelList(Set<String> labels) {
        NbtList nbtList = new NbtList();
        for (String label : labels) {
            nbtList.add(NbtString.of(label));
        }
        pokemon.getPersistentData().remove("YuLabelList");
        pokemon.getPersistentData().put("YuLabelList", nbtList);
    }

    public boolean getLabelBoolean(String key) {
        return listLabel.contains(key);
    }

    public void addLabel(String... key) {
        for (String s : key) {
            if (!s.contains("@")) {
                listLabel.add(s);
            } else {
                String[] strings = BukkitAPI.onSetString(s, "@");
                listLabel.removeIf(s1 -> s1.contains(strings[0]));
                listLabel.add(s);
            }
        }
        saveLabelList(listLabel);
    }

    public void removeLabel(String key) {
        listLabel.remove(key);
        saveLabelList(listLabel);
    }

    public boolean isLabel() {
        return listLabel.isEmpty();
    }

    private Set<String> getListLabel() {
        if (nbt.contains("YuLabelList", 9)) {
            NbtList nbtList = nbt.getList("YuLabelList", NbtElement.STRING_TYPE); // 8是NbtString的类型ID
            Set<String> result = new HashSet<>();
            // 遍历NbtList并将每个元素转换为String
            for (int i = 0; i < nbtList.size(); i++) {
                result.add(nbtList.getString(i));
            }
            return result;
        }
        // 如果没有找到标签或类型不匹配，返回空列表
        return new HashSet<>();
    }

    public Set<String> getLabel() {
        return listLabel;
    }

    //获取标签值
    public String getLabelContainsBoolean(String key) {
        for (String s : listLabel) {
            if (s.contains(key)) {
                String[] strings = BukkitAPI.onSetString(s, "@");
                if (strings.length == 2) {
                    return strings[1];
                } else {
                    return "no";
                }
            }
        }
        return "no";
    }

}
