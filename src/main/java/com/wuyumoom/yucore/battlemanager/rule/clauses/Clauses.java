package com.wuyumoom.yucore.battlemanager.rule.clauses;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Clauses {

    //包包条款：玩家不可以在战斗时使用背包中的道具
    public Boolean bagClause ;
    //接力棒条款：禁止拥有接棒招式的宝可梦
    public Boolean batonPassClause ;
    //最大一次接力棒条款
    public Boolean maxOneBatonPass ;
    //喋喋不休规则：禁止拥有喋喋不休招式的宝可梦
    public Boolean chatter ;
    //降雨规则：禁止拥有降雨特性的宝可梦
    public Boolean drizzle ;
    //降雨-悠游自如规则：队伍中不可同时存在降雨特性和悠游自如特性
    public Boolean drizzleSwim ;
    //日照规则：禁止拥有日照特性的宝可梦。
    public Boolean drought;
    //无限战斗规则：禁止能让战斗无限进行的宝可梦，具体指携带苹野果，并学会回收利用，并学会投掷或治愈波动或痛苦平分的宝可梦
    public Boolean endlessBattle ;
    //闪避特性规则：禁止拥有沙隐或雪隐特性的宝可梦。
    public Boolean evasionAbility ;
    //闪避招式规则：禁止拥有影子分身或变小招式的宝可梦
    public Boolean evasion ;
    //携带物品规则：队伍中不可携带重复物品
    public Boolean noDuplicateItems;
    //神兽规则：禁止使用神兽
    public Boolean legendBan ;
    //超级进化规则：禁止携带超级进化石
    public Boolean megaStone ;
    //心情不定规则：禁止拥有心情不定特性的宝可梦
    public Boolean moody ;
    //一击必杀规则：禁止拥有一击必杀招式的宝可梦。一击必杀招式包括：地裂，断头钳，角钻，绝对零度
    public Boolean ohKo ;
    //沙暴规则：禁止拥有沙暴特性的宝可梦
    public Boolean sandStream ;
    //踩影规则：禁止拥有踩影特性的宝可梦
    public Boolean shadowTag;
    //催眠规则：战斗中每个队伍只能有一只宝可梦陷入睡眠状态。自己使用睡觉也会触发此规则
    public Boolean sleepClause ;
    //破壳规则：禁止同时拥有破壳和接棒招式的宝可梦
    public Boolean smashPass;
    //冰雹规则：禁止拥有降雪特性的宝可梦。
    public Boolean snowWarning ;
    //心之水滴规则：禁止携带心之水滴的拉帝亚斯或拉帝欧斯
    public Boolean soulDew ;
    //种类规则：队伍中不可存在重复的宝可梦
    public Boolean multipleSpecies ;
    //虚张声势规则：禁止拥有虚张声势招式的宝可梦
    public Boolean swagger ;
    //天气加速规则：队伍中不可同时存在降雨特性和悠游自如特性 或 日照特性和叶绿素特性 或 沙暴特性和拨沙特性。
    public Boolean weatherSpeed ;
    //异兽规则：禁止使用异兽。
    public Boolean typeBan ;
    // 幻兽规则：禁止使用幻兽。
    public Boolean wildBan ;
    //一只神兽规则
    public Boolean oneLegend ;
    // 一只异兽规则
    public Boolean oneWild;
    // 一直幻兽规则
    public Boolean oneLegendWild;
    // 闪光规则:禁止闪光精灵
    public Boolean flashBan;

    public Clauses(YamlConfiguration yamlConfiguration) {
        bagClause = yamlConfiguration.getBoolean("Clauses.bagClause");
        batonPassClause = yamlConfiguration.getBoolean("Clauses.batonPassClause");
        maxOneBatonPass = yamlConfiguration.getBoolean("Clauses.maxOneBatonPass");
        chatter = yamlConfiguration.getBoolean("Clauses.chatter");
        drizzle = yamlConfiguration.getBoolean("Clauses.drizzle");
        drizzleSwim = yamlConfiguration.getBoolean("Clauses.drizzleSwim");
        drought = yamlConfiguration.getBoolean("Clauses.drought");
        endlessBattle = yamlConfiguration.getBoolean("Clauses.endlessBattle");
        evasionAbility = yamlConfiguration.getBoolean("Clauses.evasionAbility");
        evasion = yamlConfiguration.getBoolean("Clauses.evasion");
        noDuplicateItems = yamlConfiguration.getBoolean("Clauses.noDuplicateItems");
        legendBan = yamlConfiguration.getBoolean("Clauses.legendBan");
        megaStone = yamlConfiguration.getBoolean("Clauses.megaStone");
        moody = yamlConfiguration.getBoolean("Clauses.moody");
        ohKo = yamlConfiguration.getBoolean("Clauses.ohKo");
        sandStream = yamlConfiguration.getBoolean("Clauses.sandStream");
        shadowTag = yamlConfiguration.getBoolean("Clauses.shadowTag");
        sleepClause = yamlConfiguration.getBoolean("Clauses.sleepClause");
        smashPass = yamlConfiguration.getBoolean("Clauses.smashPass");
        snowWarning = yamlConfiguration.getBoolean("Clauses.snowWarning");
        multipleSpecies = yamlConfiguration.getBoolean("Clauses.multipleSpecies");
        swagger = yamlConfiguration.getBoolean("Clauses.swagger");
        weatherSpeed = yamlConfiguration.getBoolean("Clauses.weatherSpeed");
        typeBan = yamlConfiguration.getBoolean("Clauses.typeBan");
        wildBan = yamlConfiguration.getBoolean("Clauses.wildBan");
        oneLegend = yamlConfiguration.getBoolean("Clauses.oneLegend");
        oneWild = yamlConfiguration.getBoolean("Clauses.oneWild");
        oneLegendWild = yamlConfiguration.getBoolean("Clauses.oneLegendWild");
        flashBan = yamlConfiguration.getBoolean("Clauses.flashBan");
        soulDew = yamlConfiguration.getBoolean("Clauses.soulDew");
    }
    //规则判断
    //是否重复携带
    public boolean isDuplicateItem(List<String> items) {
        return items.size() != new HashSet<>(items).size();
    }
    //是否存在重复元素
    public boolean isElementRepeated(List<String> list, String targetElement) {
        return Collections.frequency(list, targetElement) > 1;
    }
    //是否重复
    public boolean isRepeated(List<String> list, String target,String string){
        return list.contains(target) && list.contains(string);
    }
}
