package com.wuyumoom.yucore.battlemanager.rule;

import com.cobblemon.mod.common.api.moves.Move;
import com.cobblemon.mod.common.api.pokemon.labels.CobblemonPokemonLabels;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.BattleFormat;
import com.cobblemon.mod.common.battles.BattleRules;
import com.cobblemon.mod.common.battles.BattleType;
import com.cobblemon.mod.common.battles.BattleTypes;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import com.wuyumoom.yucore.YuCore;
import com.wuyumoom.yucore.api.BukkitAPI;
import com.wuyumoom.yucore.api.pokemon.PokemonAPI;
import com.wuyumoom.yucore.battlemanager.event.YuAddRule;
import com.wuyumoom.yucore.battlemanager.rule.clauses.Clauses;
import com.wuyumoom.yucore.battlemanager.rule.message.BattleMessage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Rule extends Clauses {


    private static Map<Plugin,Map<String, Rule>> ruleMap = new HashMap<>();

    private final YamlConfiguration yamlConfiguration;
    private final String name;
    private final BattleFormat battleFormat;
    private String levelMode = "强制";
    private String level ="50";
    private final Set<String> ruleSet = new HashSet<>();
    private String battleType = "单打";
    private Boolean fullHeal = true;
    private List<String> winCommands = new ArrayList<>();
    private List<String> loseCommands = new ArrayList<>();

    //开战时提示
    private int matching_Second = 5;
    private final String matching_Message;
    //开战时指令
    private List<String> matching_Commands = new ArrayList<>();


    private final int[] pokemonAmount;
    public List<String> banPokeList;
    public List<String> banMoveList;
    public List<String> banItemList;
    public List<String> banFormList;
    public List<String> banAbility;
    private final BattleMessage message;

    public Rule(YamlConfiguration yamlConfiguration, String name, BattleMessage message, Plugin  plugin) {
        super(yamlConfiguration);
        this.message = message;
        this.yamlConfiguration = yamlConfiguration;
        this.name = name;
        ruleSet.add(BattleRules.OBTAINABLE);
        ruleSet.add(BattleRules.PAST);
        ruleSet.add(BattleRules.UNOBTAINABLE);
        battleFormat = createBattleFormat();
        levelMode = yamlConfiguration.getString("level.mode");
        level = yamlConfiguration.getString("level.range");
        battleType = yamlConfiguration.getString("battleType");
        fullHeal = yamlConfiguration.getBoolean("fullHeal");
        matching_Second = yamlConfiguration.getInt("Matching.second");
        matching_Message = BukkitAPI.onReplace(yamlConfiguration.getString("Matching.Message"));
        matching_Commands = yamlConfiguration.getStringList("Matching.Commands");
        winCommands = yamlConfiguration.getStringList("Winner.Commands");
        loseCommands = yamlConfiguration.getStringList("Losers.Commands");

        pokemonAmount = BukkitAPI.onSetInt(Objects.requireNonNull(yamlConfiguration.getString("pokemonAmount")));
        banPokeList = yamlConfiguration.getStringList("banList.banPokemon");
        banMoveList = yamlConfiguration.getStringList("banList.banMove");
        banItemList = yamlConfiguration.getStringList("banList.banHeldItem");
        banAbility = yamlConfiguration.getStringList("banList.banAbility");
        addRule();
        addRuleMap(plugin, name,this);
    }
    public static void addRuleMap(Plugin plugin, String ruleName, Rule rule) {
        ruleMap.computeIfAbsent(plugin, k -> new HashMap<>()).put(ruleName, rule);
        Bukkit.getPluginManager().callEvent(new YuAddRule());
    }

    public static Map<Plugin, Map<String, Rule>> getRuleMap() {
        return ruleMap;
    }

    private void addRule(){
        if (batonPassClause){
            banMoveList.add("接棒");
        }
        if (chatter){
            banMoveList.add("喋喋不休");
        }
        if (drizzle){
            banAbility.add("降雨");
        }
        if (drought){
            banAbility.add("日照");
        }
        if (endlessBattle){
            ruleSet.add(BattleRules.ENDLESS_BATTLE_CLAUSE);
        }
        if (evasionAbility){
            banAbility.add("雪隐");
            banAbility.add("沙隐");
        }
        if (evasion){
            banMoveList.add("影子分身");
            banMoveList.add("变小");
        }
        if (moody){
            banAbility.add("心情不定");
        }
        if (ohKo){
            banMoveList.add("地裂");
            banMoveList.add("断头钳");
            banMoveList.add("角钻");
            banMoveList.add("绝对零度");
        }
        if (sandStream){
            banAbility.add("沙暴");
        }
        if (shadowTag){
            banAbility.add("踩影");
        }
        if (sleepClause){
            ruleSet.add(BattleRules.SLEEP_CLAUSE);
        }
        if (snowWarning){
            banAbility.add("降雪");
        }
        if (swagger){
            banMoveList.add("虚张声势");
        }
    }
    public boolean isRule(Player player){
//        if (!FileLoader.getWorld().contains(player.getWorld().getName())){
//            BukkitAPI.sendMessage(Message.getNoWorld(), player);
//            return false;
//        }
        PlayerPartyStore party = PlayerExtensionsKt.party(Objects.requireNonNull(PlayerExtensionsKt.getPlayer(player.getUniqueId())));
        int size = party.toBattleTeam().size();
        if (size < pokemonAmount[0] || size > pokemonAmount[1]) {
            return false;
        }
        List<String> moveList = new ArrayList<>();
        List<String> ability = new ArrayList<>();
        List<String> item = new ArrayList<>();
        List<String> pokeName = new ArrayList<>();
        int legend = 0;
        int wild = 0;
        int LegendWild = 0;
        for (Pokemon pokemon : party) {
            if (levelMode.equalsIgnoreCase("范围")) {
                if (isLevel(pokemon, player, BukkitAPI.onSetInt(level))) {
                    BukkitAPI.sendMessage(message.noPokemonAmount(), player);
                    return false;
                }
            }
            if (flashBan){
                if (pokemon.getShiny()){
                    BukkitAPI.sendMessage(message.noRule(), player);
                    return false;
                }
            }
            if (oneLegendWild){
                if (pokemon.hasLabels(CobblemonPokemonLabels.MYTHICAL)){
                    LegendWild++;
                }
                if (LegendWild > 1){
                    BukkitAPI.sendMessage(message.noRule(), player);
                    return false;
                }
            }
            if (oneWild){
                if (pokemon.hasLabels(CobblemonPokemonLabels.ULTRA_BEAST)){
                    wild++;
                }
                if (wild > 1){
                    BukkitAPI.sendMessage(message.noRule(), player);
                    return false;
                }
            }
            if (oneLegend){
                if (pokemon.hasLabels(CobblemonPokemonLabels.LEGENDARY)){
                    legend++;
                }
                if (legend > 1){
                    BukkitAPI.sendMessage(message.noRule(), player);
                    return false;
                }
            }
            if (wildBan){
                if (pokemon.hasLabels(CobblemonPokemonLabels.MYTHICAL)){
                    BukkitAPI.sendMessage(message.noRule(), player);
                    return false;
                }
            }
            if (typeBan){
                if (pokemon.hasLabels(CobblemonPokemonLabels.ULTRA_BEAST)){
                    BukkitAPI.sendMessage(message.noRule(), player);
                    return false;
                }
            }
            if (legendBan){
                if (pokemon.hasLabels(CobblemonPokemonLabels.LEGENDARY)){
                    BukkitAPI.sendMessage(message.noRule(), player);
                    return false;
                }
            }
            if (isBanPoke(pokemon, player,pokeName) ||
                    isBanMove(pokemon, player, moveList) ||
                    isBanItem(pokemon, player,item) ||
                    isAbility(pokemon, player, ability)
            ) {
                return false;
            }
        }
        if (weatherSpeed){
            if (isRepeated(banAbility,"降雨","悠游自如") ||
                    isRepeated(banAbility,"日照","叶绿素")||
                    isRepeated(banAbility," 沙暴","拨沙")
            ) {
                BukkitAPI.sendMessage(message.noRule(), player);
                return false;
            }
        }
        if (multipleSpecies){
            if (isDuplicateItem(pokeName)){
                BukkitAPI.sendMessage(message.noRule(), player);
                return false;
            }
        }
        if (smashPass){
            if (isRepeated(banMoveList, "破壳","接棒")){
                BukkitAPI.sendMessage(message.noRule(), player);
                return false;
            }
        }
        if (noDuplicateItems){
            if (isDuplicateItem(item)){
                BukkitAPI.sendMessage(message.noRule(), player);
                return false;
            }
        }
        if (maxOneBatonPass) {
            if (isElementRepeated(moveList, "接棒")) {
                BukkitAPI.sendMessage(message.noRule(), player);
                return false;
            }
        }
        if (drizzleSwim){
            if (isRepeated(banAbility,"降雨","悠游自如")) {
                BukkitAPI.sendMessage(message.noRule(), player);
                return false;
            }
        }
        return true;
    }


    private boolean isBanItem(Pokemon pokemon, Player player, List<String> item) {
        String s = PokemonAPI.onGetHeldItemName(pokemon);
        if (!s.equalsIgnoreCase("无")){
            item.add(s);
        }
        if (megaStone){
            if (s.contains("超级进化石")){
                return false;
            }
        }
        if (banItemList.contains(s)) {
            BukkitAPI.sendMessage(message.banHeldItem(), player);
            return true;
        }
        return false;
    }

    private Boolean isBanMove(Pokemon pokemon, Player player, List<String> moveList) {
        for (Move move : pokemon.getMoveSet().getMoves()) {
            String s = PokemonAPI.onGetTranslatePath(move.getTemplate().getDisplayName());
            if (!s.equalsIgnoreCase("无")){
                moveList.add(s);
            }
            if (banMoveList.contains(s)) {
                BukkitAPI.sendMessage(message.banMove(), player);
                return true;
            }
        }
        return false;
    }

    private Boolean isBanPoke(Pokemon pokemon, Player player, List<String> pokeName) {
        String s = PokemonAPI.onGetTranslatePath(pokemon.getSpecies().getTranslatedName());
        pokeName.add(s);
        if (banPokeList.contains(s)) {
            BukkitAPI.sendMessage(message.banPoke(), player);
            return true;
        }
        return false;
    }
    private Boolean isAbility(Pokemon pokemon, Player player, List<String> ability) {
        String s = YuCore.getCobblemon().getZh().get(pokemon.getAbility().getDisplayName());
        ability.add(s);
        if (banAbility.contains(s)) {
            BukkitAPI.sendMessage(message.banAbility(), player);
            return true;
        }
        return false;
    }


    private boolean isLevel(Pokemon pokemon, Player player, int[] level) {
        if (pokemon.getLevel() > level[0] || pokemon.getLevel() < level[1]) {
            BukkitAPI.sendMessage(message.noPokemonLevel(), player);
            return true;
        }
        return false;
    }


    private BattleFormat createBattleFormat() {
        if (levelMode.equalsIgnoreCase("范围")) {
            return new BattleFormat(
                    "cobblemon",
                    isBattleTypes(),
                    ruleSet,
                    9,
                    0
            );
        }
        return new BattleFormat(
                "cobblemon",
                isBattleTypes(),
                ruleSet,
                9,
                Integer.parseInt(level)
        );
    }
    private BattleType isBattleTypes() {
        if (battleType.equalsIgnoreCase("单打")) {
            return BattleTypes.INSTANCE.getSINGLES();
        }
        if (battleType.equalsIgnoreCase("双打")) {
            return BattleTypes.INSTANCE.getDOUBLES();
        }
        return BattleTypes.INSTANCE.getTRIPLES();
    }

    public YamlConfiguration getYamlConfiguration() {
        return yamlConfiguration;
    }

    public BattleFormat getBattleFormat() {
        return battleFormat;
    }

    public String getName() {
        return name;
    }
    public boolean isFullHeal() {
        return fullHeal;
    }

    public int getMatching_Second() {
        return matching_Second;
    }

    public List<String> getMatching_Commands() {
        return matching_Commands;
    }

    public List<String> getLoseCommands() {
        return loseCommands;
    }

    public List<String> getWinCommands() {
        return winCommands;
    }

    public String getMatching_Message() {
        return matching_Message;
    }
}

