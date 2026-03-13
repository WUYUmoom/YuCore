package com.wuyumoom.yucore.api.pokemon

import com.cobblemon.mod.common.api.callback.PartySelectCallbacks
import com.cobblemon.mod.common.api.callback.PartySelectPokemonDTO
import com.cobblemon.mod.common.api.text.gray
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.party
import net.minecraft.component.Component
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import kotlin.collections.map
import kotlin.collections.toMutableList


fun ServerPlayerEntity.openPartyWithCallback(
    title: String = "请选择要使用的精灵",
    hoverText: MutableList<String> = mutableListOf(),
    enabled: (Pokemon) -> Boolean = { true },
    callback: (Pokemon) -> Unit
) {
    val pokemon = this.party().toMutableList()
    val pokemonWithHoverText = pokemon.map { pk ->
        PartySelectPokemonDTO(
            pokemon = pk,
            enabled = enabled(pk),
            hoverText = hoverText.map { Text.of(PokemonAPI.onReplace( it, pk)) }
        )
    }
    PartySelectCallbacks.create(
        player = this,
        title = Text.of(title),
        pokemon = pokemonWithHoverText
    ) { _, index ->
        val selectedPokemon = pokemon[index]
        callback(selectedPokemon)
    }
}
