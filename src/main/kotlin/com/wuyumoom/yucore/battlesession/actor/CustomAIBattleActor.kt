package com.wuyumoom.yucore.battlesession.actor

import com.cobblemon.mod.common.api.battles.model.actor.AIBattleActor
import com.cobblemon.mod.common.api.battles.model.actor.ActorType
import com.cobblemon.mod.common.api.battles.model.actor.EntityBackedBattleActor
import com.cobblemon.mod.common.api.battles.model.actor.FleeableBattleActor
import com.cobblemon.mod.common.api.battles.model.ai.BattleAI
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3
import java.util.UUID

class CustomAIBattleActor(
    private val displayName: String,
    uuid: UUID,
    pokemonList: List<BattlePokemon>,
    battleAI: BattleAI,
    override val entity: LivingEntity
) : AIBattleActor(uuid, pokemonList, battleAI), EntityBackedBattleActor<LivingEntity>, FleeableBattleActor {

    private val world: ServerLevel
    private val posSnapshot: Vec3

    init {
        val lvl = entity.level()
        require(lvl is ServerLevel) { "CustomAIBattleActor requires ServerLevel, got ${lvl::class.java.name}" }
        world = lvl
        posSnapshot = entity.position() // 初始位置快照
    }

    override val type: ActorType = ActorType.NPC

    // Cobblemon 这里期望的是“初始位置”对象；你要实时则改为 entity.position()
    override val initialPos: Vec3 = posSnapshot

    override fun getName(): MutableComponent = Component.literal(displayName)

    override fun nameOwned(name: String): MutableComponent = Component.literal(name)

    override val fleeDistance: Float get() = 20.0F

    // 如果你希望返回“实时位置”，把第二个返回值改为 entity.position()
    override fun getWorldAndPosition(): Pair<ServerLevel, Vec3> = Pair(world, entity.position())
}