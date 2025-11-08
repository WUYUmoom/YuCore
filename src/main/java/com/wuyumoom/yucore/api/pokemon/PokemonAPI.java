package com.wuyumoom.yucore.api.pokemon;

import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonPropertyExtractor;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.api.pokemon.stats.Stat;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore;
import com.cobblemon.mod.common.battles.BattleRegistry;
import com.cobblemon.mod.common.command.argument.PartySlotArgumentType;
import com.cobblemon.mod.common.command.argument.PokemonPropertiesArgumentType;
import com.cobblemon.mod.common.pokemon.FormData;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.pokemon.Species;
import com.cobblemon.mod.common.pokemon.properties.UncatchableProperty;
import com.cobblemon.mod.common.util.MiscUtilsKt;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.wuyumoom.yucore.YuCore;
import com.wuyumoom.yucore.api.BukkitAPI;
import com.wuyumoom.yucore.api.pokemon.base.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PokemonAPI {

    //将宝可梦转换为nbt
    public static String getPokemonNBT(Pokemon pokemon,ServerPlayerEntity serverPlayerEntity){
        NbtCompound nbtTagCompound = new NbtCompound();
        nbtTagCompound = pokemon.saveToNBT(serverPlayerEntity.getRegistryManager(), nbtTagCompound);
        return nbtTagCompound.toString();
    }
    @Deprecated
    public static Pokemon onSetForPokemon(Pokemon pokemon, String[] strings) {
        for (String string : strings) {
            try {
                onSetPokemon(pokemon, string);
            } catch (Exception e) {
                System.out.println("参数错误:"+string);
            }

        }
        return pokemon;
    }
    @Deprecated
    public static void onSetPokemon(Pokemon pokemon, String string) throws Exception {
        String[] actor = BukkitAPI.onSetString(string, ":");
        switch (actor[0]) {
            case "标签":
                PokemonLabel.getInstance(pokemon).addLabel(actor[1]);
                break;
            case "随机IVS":
                for (int i = 1; i < Integer.parseInt(actor[1]); i++) {
                    pokemon.setIV(getRandomStat(), 31);
                }
                break;
            case "技能":
                pokemon.getMoveSet().setMove(0, YuMove.getMove(actor[1]));
                break;
            case "捕捉":
                if (!Boolean.getBoolean(actor[1])) {
                    UncatchableProperty.INSTANCE.uncatchable().apply(pokemon);
                }
                break;
            case "亲密度":
                pokemon.setFriendship(Integer.parseInt(actor[1]), true);
                break;
            case "形态":
                if (actor[1].equals("默认")){
                    pokemon.updateAspects();
                    break;
                }
                PokemonProperties parse = PokemonProperties.Companion.parse(pokemon.getSpecies().getTranslatedName().getString()+" "+"aspect="+actor[1].toLowerCase(Locale.ENGLISH));
                parse.apply(pokemon);
                break;
            case "Ability":
            case "特性":
                pokemon.setAbility$common(YuAbility.getAbility(actor[1]));
                break;
            case "PokeBall":
            case "球种":
                pokemon.setCaughtBall(YuPokeBall.getPokeBall(actor[1]));
                break;
            case "Shiny":
            case "闪光":
                pokemon.setShiny(actor[1].equalsIgnoreCase("true"));
                break;
            case "Nature":
            case "性格":
                pokemon.setNature(YuNature.getNature(actor[1]));
                break;
            case "Gender":
            case "性别":
                if (actor[1].equals("雄")) {
                    pokemon.setGender(Gender.MALE);
                    break;
                }
                if (actor[1].equals("雌")) {
                    pokemon.setGender(Gender.FEMALE);
                    break;
                }
                pokemon.setGender(Gender.GENDERLESS);
                break;
            case "Level":
            case "等级":
                pokemon.setLevel(Integer.parseInt(actor[1]));
                break;
            case "StatsAll":
            case "6v":
                for (Stat stat : onGetIVS()) {
                    pokemon.getIvs().set(stat, Integer.parseInt(actor[1]));
                }
                break;
            case "HP":
            case "血量":
                pokemon.setIV(Stats.HP, Integer.parseInt(actor[1]));
                break;
            case "ATTACK":
            case "攻击":
                pokemon.setIV(Stats.ATTACK, Integer.parseInt(actor[1]));
                break;
            case "DEFENCE":
            case "防御":
                pokemon.setIV(Stats.DEFENCE, Integer.parseInt(actor[1]));
                break;
            case "SPEED":
            case "速度":
                pokemon.setIV(Stats.SPEED, Integer.parseInt(actor[1]));
                break;
            case "SPECIAL_DEFENCE":
            case "特防":
                pokemon.setIV(Stats.SPECIAL_DEFENCE, Integer.parseInt(actor[1]));
                break;
            case "SPECIAL_ATTACK":
            case "特攻":
                pokemon.setIV(Stats.SPECIAL_ATTACK, Integer.parseInt(actor[1]));
                break;
            case "Tradeable":
            case "交易":
                pokemon.setTradeable(Boolean.parseBoolean(actor[1]));
                break;
            default:
                System.out.println(actor[1] + "参数错误");
                break;
        }
    }

    //获取随机属性就是血量什么的
    public static Stat getRandomStat() {
        List<Stat> stats = new ArrayList<>(onGetIVS());
        return stats.get(new Random().nextInt(stats.size()));
    }

    //获取全部属性
    public static Collection<Stat> onGetIVS() {
        return Cobblemon.INSTANCE.getStatProvider().ofType(Stat.Type.PERMANENT);
    }

    //给玩家一个宝可梦
    public static boolean onGivePokemon(Player player, Pokemon pokemon) {
        ServerPlayerEntity serverPlayerEntity = PlayerExtensionsKt.getPlayer(player.getUniqueId());
        if (serverPlayerEntity == null) {
            return false;
        }
        PlayerPartyStore playerPartyStore = PlayerExtensionsKt.party(serverPlayerEntity);
        playerPartyStore.add(pokemon);
        return true;
    }

    //获取玩家队伍从0开始
    public static Map<Integer, Pokemon> getTeamPokemon(PlayerPartyStore player) {
        Map<Integer, Pokemon> teamPokemon = new HashMap<>();
        for (int i = 0; i < 6; i++) {
            if (player.get(i) != null) {
                teamPokemon.put(i, player.get(i));
            } else {
                teamPokemon.put(i, null);
            }
        }
        return teamPokemon;
    }

    //获取宝可梦多少v
    public static int isIvs(Pokemon pokemon) {
        int i = 0;
        for (Stat value : onGetIVS()) {
            if (pokemon.getIvs().get(value) >= 31) {
                i++;
            }
        }
        return i;
    }

    public static String getIVS_SUM(Pokemon pokemon) {
        int ivs = pokemon.getIvs().get(Stats.HP) + pokemon.getIvs().get(Stats.ATTACK) + pokemon.getIvs().get(Stats.DEFENCE) + pokemon.getIvs().get(Stats.SPECIAL_ATTACK) + pokemon.getIvs().get(Stats.SPECIAL_DEFENCE) + pokemon.getIvs().get(Stats.SPEED);
        DecimalFormat df = new DecimalFormat("#0.##");
        return df.format((int) (ivs / 186.0D * 100.0D)) + "%";
    }

    public static String getEVS_SUM(Pokemon pokemon) {
        int evs = pokemon.getEvs().get(Stats.HP) + pokemon.getEvs().get(Stats.ATTACK) + pokemon.getEvs().get(Stats.DEFENCE) + pokemon.getEvs().get(Stats.SPECIAL_ATTACK) + pokemon.getEvs().get(Stats.SPECIAL_DEFENCE) + pokemon.getEvs().get(Stats.SPEED);
        DecimalFormat df = new DecimalFormat("#0.##");
        return df.format((int) (evs / 510.0D * 100.0D)) + "%";
    }

    public static String onGetTranslatePath(Object path) {
        if (!YuCore.isIsTranslatePath()) {
            return ((MutableText) path).getString();
        }
        Pattern pattern = Pattern.compile("'(.*?)'");
        Matcher matcher = pattern.matcher(path.toString().replaceAll(" ", ""));
        if (matcher.find()) {
            String s = YuCore.getCobblemon().getZh().get(matcher.group(1));
            if (s != null) {
                return s; // 返回第一个匹配组的内容（不包含单引号）
            }
        }
        String s = YuCore.getCobblemon().getZh().get(((MutableText) path).getString());
        if (s != null) {
            return s;
        }
        return ((MutableText) path).getString();
    }

    public static @Nullable Pokemon onGetTeamPokemon(Player player, int index) {
        if (index < 0 || index > 5) {
            YuCore.getInstance().getServer().getLogger().info("index错误" + index);
            return null;
        }
        ServerPlayerEntity serverPlayerEntity = PlayerExtensionsKt.getPlayer(player.getUniqueId());
        if (serverPlayerEntity == null) {
            return null;
        }
        return PlayerExtensionsKt.party(serverPlayerEntity).get(index);
    }

    public static String onReplace(String s, Pokemon pokemon) {
        if (s == null) {
            return ""; // 或者返回默认值，避免 NullPointerException
        }
        return s.replaceAll(Pattern.quote("%Move1%"), Matcher.quoteReplacement(YuMove.getMoveSkills(pokemon, 0)))
                .replaceAll(Pattern.quote("%Move2%"), Matcher.quoteReplacement(YuMove.getMoveSkills(pokemon, 1)))
                .replaceAll(Pattern.quote("%Move3%"), Matcher.quoteReplacement(YuMove.getMoveSkills(pokemon, 2)))
                .replaceAll(Pattern.quote("%Move4%"), Matcher.quoteReplacement(YuMove.getMoveSkills(pokemon, 3)))
                .replaceAll(Pattern.quote("%Growth%"), Matcher.quoteReplacement(PokemonLabel.getInstance(pokemon).getLabelContainsBoolean("YuGrowth").equalsIgnoreCase("no") ? "未知" : PokemonLabel.getInstance(pokemon).getLabelContainsBoolean("YuGrowth")))
                .replaceAll(Pattern.quote("%Owner%"), Matcher.quoteReplacement(pokemon.getOwnerPlayer() == null ? "无" : pokemon.getOwnerPlayer().getName().getString()))
                .replaceAll(Pattern.quote("%pokemon_name%"), Matcher.quoteReplacement(onGetTranslatePath(pokemon.getSpecies().getTranslatedName())))
                .replaceAll(Pattern.quote("%Level%"), Matcher.quoteReplacement(String.valueOf(pokemon.getLevel())))
                .replaceAll(Pattern.quote("%IVS_SUM%"), Matcher.quoteReplacement(getIVS_SUM(pokemon)))
                .replaceAll(Pattern.quote("%IVS_HP%"), Matcher.quoteReplacement(String.valueOf(pokemon.getIvs().get(YuStats.getStats().get("血量")))))
                .replaceAll(Pattern.quote("%IVS_Attack%"), Matcher.quoteReplacement(String.valueOf(pokemon.getIvs().get(YuStats.getStats().get("攻击")))))
                .replaceAll(Pattern.quote("%IVS_Speed%"), Matcher.quoteReplacement(String.valueOf(pokemon.getIvs().get(YuStats.getStats().get("速度")))))
                .replaceAll(Pattern.quote("%IVS_Defence%"), Matcher.quoteReplacement(String.valueOf(pokemon.getIvs().get(YuStats.getStats().get("防御")))))
                .replaceAll(Pattern.quote("%IVS_SpecialAttack%"), Matcher.quoteReplacement(String.valueOf(pokemon.getIvs().get(YuStats.getStats().get("特攻")))))
                .replaceAll(Pattern.quote("%IVS_SpecialDefence%"), Matcher.quoteReplacement(String.valueOf(pokemon.getIvs().get(YuStats.getStats().get("特防")))))
                .replaceAll(Pattern.quote("%EVS_SUM%"), Matcher.quoteReplacement(getEVS_SUM(pokemon)))
                .replaceAll(Pattern.quote("%EVS_HP%"), Matcher.quoteReplacement(String.valueOf(pokemon.getEvs().get(YuStats.getStats().get("血量")))))
                .replaceAll(Pattern.quote("%EVS_Attack%"), Matcher.quoteReplacement(String.valueOf(pokemon.getEvs().get(YuStats.getStats().get("攻击")))))
                .replaceAll(Pattern.quote("%EVS_Speed%"), Matcher.quoteReplacement(String.valueOf(pokemon.getEvs().get(YuStats.getStats().get("速度")))))
                .replaceAll(Pattern.quote("%EVS_Defence%"), Matcher.quoteReplacement(String.valueOf(pokemon.getEvs().get(YuStats.getStats().get("防御")))))
                .replaceAll(Pattern.quote("%EVS_SpecialAttack%"), Matcher.quoteReplacement(String.valueOf(pokemon.getEvs().get(YuStats.getStats().get("特攻")))))
                .replaceAll(Pattern.quote("%EVS_SpecialDefence%"), Matcher.quoteReplacement(String.valueOf(pokemon.getEvs().get(YuStats.getStats().get("特防")))))
                .replaceAll(Pattern.quote("%Shiny%"), Matcher.quoteReplacement(pokemon.getShiny() ? "闪光" : "普通"))
                .replaceAll(Pattern.quote("%Nature%"), Matcher.quoteReplacement(onGetTranslatePath(MiscUtilsKt.asTranslated(pokemon.getNature().getDisplayName()))))
                .replaceAll(Pattern.quote("%Ability%"), Matcher.quoteReplacement(onGetTranslatePath(MiscUtilsKt.asTranslated(pokemon.getAbility().getDisplayName()))))
                .replaceAll(Pattern.quote("%Gender%"), Matcher.quoteReplacement(YuGender.onGetGender(pokemon)))
                .replaceAll(Pattern.quote("%Nick_Name%"), Matcher.quoteReplacement((pokemon.getNickname() == null) ? "未设置" : pokemon.getNickname().getString()))
                .replaceAll(Pattern.quote("%HeldItem%"), Matcher.quoteReplacement(onGetHeldItemName(pokemon)))
                .replaceAll(Pattern.quote("%UUID%"), Matcher.quoteReplacement(pokemon.getUuid().toString()));
    }

    public static String onGetHeldItemName(Pokemon pokemon) {
        if (!pokemon.getHeldItem$common().isEmpty()) {
            return pokemon.getHeldItem$common().getName().getString();
        } else {
            return "无";
        }
    }

    //发送对战面板消息
    public static void onBattleBroadcastChatMessage(Player player, String message) {
        PokemonBattle battleByParticipatingPlayerId = BattleRegistry.INSTANCE.getBattleByParticipatingPlayerId(player.getUniqueId());
        if (battleByParticipatingPlayerId != null) {
            battleByParticipatingPlayerId.getActors().iterator().next().sendMessage(Text.literal(message));
        }
    }

    public static Boolean onSpawnPoke(Entity pokemonEntity, World world) {
        return ((CraftWorld) world).getHandle().addFreshEntity(pokemonEntity, CreatureSpawnEvent.SpawnReason.SPAWNER);
    }

    public static Pokemon onNBTLoadPokemon(String player, String nbt) {
        try {
            return Pokemon.Companion.loadFromNBT(
                    PlayerExtensionsKt.getPlayer(Bukkit.getOfflinePlayer(player).getUniqueId()).getRegistryManager(),
                    StringNbtReader.parse(nbt)
            );
        } catch (CommandSyntaxException | IllegalStateException var3) {
            YuCore.getInstance().getServer().getLogger().severe("加载宝可梦 NBT 失败！错误字段可能包含非法 Stat 类型（如 accuracy）");
            var3.printStackTrace();
            return null;
        }
    }

}
