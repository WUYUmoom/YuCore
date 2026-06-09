package com.wuyumoom.yucore.battlesession

import com.cobblemon.mod.common.api.battles.model.PokemonBattle
import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.api.npc.NPCClasses
import com.cobblemon.mod.common.battles.BattleFormat
import com.cobblemon.mod.common.entity.npc.NPCEntity
import net.minecraft.world.level.Level
import org.bukkit.configuration.ConfigurationSection
import java.util.*

abstract class BattleSession {
    companion object {
        private val battleMap: MutableMap<UUID, BattleSession> = mutableMapOf()
        @JvmStatic
        fun getBattleSession(battleId: UUID): BattleSession? = battleMap[battleId]
    }
    /**
     * 战斗结束的回调
     */
    private val winCallbacks = mutableListOf<(PokemonBattle) -> Unit>()
    private val loseCallbacks = mutableListOf<(PokemonBattle) -> Unit>()

    fun onWin(callback: (PokemonBattle) -> Unit) = apply { winCallbacks += callback }
    fun onLose(callback: (PokemonBattle) -> Unit) = apply { loseCallbacks += callback }
    var cloneParties: Boolean = true
    var healFirst: Boolean = true
    var format: BattleFormat = BattleFormat.GEN_9_SINGLES
    abstract fun engage(): PokemonBattle?
    /**
     * 战斗结束
     */
    protected fun battleEnd(battle: PokemonBattle) {
        battleMap[battle.battleId] = this
        battle.onEndHandlers += {
            battle.winners.forEach { winners ->
                if (winners.type == ActorType.PLAYER) {
                    winCallbacks.forEach { it(battle) }
                }
            }
            battle.losers.forEach { losers ->
                if (losers.type == ActorType.PLAYER) {
                    loseCallbacks.forEach { it(battle) }
                }
            }
        }
    }

}