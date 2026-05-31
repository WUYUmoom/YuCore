package com.wuyumoom.yucore.battlesession.uitl

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.scheduling.after
import com.cobblemon.mod.common.api.types.tera.TeraTypes
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.chainFutures
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EntityDimensions
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import java.util.concurrent.CompletableFuture

private const val MIN_HORIZONTAL_DIST = 2.0

object Util {

    const val VIRTUAL_AI_MARK_KEY = "yucobblemonsession.virtual_ai"

    fun registerEvents() {
        CobblemonEvents.LOOT_DROPPED.subscribe { event ->
            val pokemonEntity = event.entity as? PokemonEntity ?: return@subscribe
            val pokemon = pokemonEntity.pokemon
            if (pokemon.persistentData.getBoolean(VIRTUAL_AI_MARK_KEY)) {
                event.drops.clear()
                event.cancel()
            }
        }

        CobblemonEvents.POKEMON_FAINTED.subscribe { event ->
            if (event.pokemon.persistentData.getBoolean(VIRTUAL_AI_MARK_KEY)) {
                event.pokemon.removeHeldItem()
            }
        }
        CobblemonEvents.EXPERIENCE_GAINED_EVENT_PRE.subscribe { event ->
            if (event.pokemon.persistentData.getBoolean(VIRTUAL_AI_MARK_KEY)) {
                event.cancel()
            }
        }
    }

    fun markVirtualAIPokemon(pokemon: Pokemon) {
        pokemon.persistentData.putBoolean(VIRTUAL_AI_MARK_KEY, true)
    }

    fun unmarkVirtualAIPokemon(pokemon: Pokemon) {
        if (pokemon.persistentData.contains(VIRTUAL_AI_MARK_KEY)) {
            pokemon.persistentData.remove(VIRTUAL_AI_MARK_KEY)
        }
    }

    fun clearVirtualAIMarks(aiTeam: List<BattlePokemon>) {
        aiTeam.forEach { battlePokemon ->
            unmarkVirtualAIPokemon(battlePokemon.originalPokemon)
            unmarkVirtualAIPokemon(battlePokemon.effectedPokemon)
        }
    }

    // ================== 公共回收 + 延迟删除 ==================
    fun recallThenDiscard(
        entities: List<PokemonEntity>,
    ) {
        val finalFuture = CompletableFuture<Unit>()
        chainFutures(
            entities.map { e -> { e.recallWithAnimation() } }.iterator(),
            finalFuture,
        )
    }

    /**
     * 查找安全的生成位置
     */
    fun findSafeSpawnPosition(
        level: ServerLevel,
        npcDimensions: EntityDimensions,
        player: ServerPlayer,
    ): Vec3? {
        val base = player.position()
        val offsets = buildCandidateOffsets(player)
        val yCandidates = listOf(0.0, 1.0, -1.0)

        for (offset in offsets) {
            val horizontal = base.add(offset)
            for (yOffset in yCandidates) {
                val candidate = Vec3(horizontal.x, horizontal.y + yOffset, horizontal.z)
                val box: AABB = npcDimensions.makeBoundingBox(candidate)
                if (isPositionSafe(level, player, box)) {
                    return candidate
                }
            }
        }
        return null
    }

    private fun isPositionSafe(
        level: ServerLevel,
        player: ServerPlayer,
        boundingBox: AABB,
    ): Boolean {
        if (!level.noCollision(boundingBox)) return false
        val colliding =
            level.getEntities(player, boundingBox) { e ->
                e is LivingEntity && e.isAlive && !e.isSpectator
            }
        return colliding.isEmpty()
    }

    fun buildCandidateOffsets(player: ServerPlayer): List<Vec3> {
        val offsets = LinkedHashSet<Vec3>()
        val look = player.lookAngle
        val horizontal = Vec3(look.x, 0.0, look.z)
        val radii = listOf(MIN_HORIZONTAL_DIST, MIN_HORIZONTAL_DIST + 1.0, MIN_HORIZONTAL_DIST + 2.0)

        if (horizontal.lengthSqr() > 1.0E-6) {
            val forward = horizontal.normalize()
            val left = Vec3(-forward.z, 0.0, forward.x)
            for (r in radii) {
                val f = forward.scale(r)
                offsets += f
                offsets += f.add(left)
                offsets += f.subtract(left)
                offsets += f.add(left.scale(2.0))
                offsets += f.subtract(left.scale(2.0))
                offsets += Vec3(f.x + left.x, 0.0, f.z + left.z).scale(1.5)
                offsets += Vec3(f.x - left.x, 0.0, f.z - left.z).scale(1.5)
            }
        }
        offsets += Vec3(MIN_HORIZONTAL_DIST, 0.0, 0.0)
        offsets += Vec3(-MIN_HORIZONTAL_DIST, 0.0, 0.0)
        offsets += Vec3(0.0, 0.0, MIN_HORIZONTAL_DIST)
        offsets += Vec3(0.0, 0.0, -MIN_HORIZONTAL_DIST)
        offsets += Vec3(MIN_HORIZONTAL_DIST + 1.0, 0.0, 0.0)
        offsets += Vec3(-(MIN_HORIZONTAL_DIST + 1.0), 0.0, 0.0)
        offsets += Vec3(0.0, 0.0, MIN_HORIZONTAL_DIST + 1.0)
        offsets += Vec3(0.0, 0.0, -(MIN_HORIZONTAL_DIST + 1.0))
        offsets += Vec3(MIN_HORIZONTAL_DIST, 0.0, MIN_HORIZONTAL_DIST)
        offsets += Vec3(MIN_HORIZONTAL_DIST, 0.0, -MIN_HORIZONTAL_DIST)
        offsets += Vec3(-MIN_HORIZONTAL_DIST, 0.0, MIN_HORIZONTAL_DIST)
        offsets += Vec3(-MIN_HORIZONTAL_DIST, 0.0, -MIN_HORIZONTAL_DIST)

        return offsets.toList()
    }
}