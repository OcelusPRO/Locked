package fr.ftnl.locked.config.dataElements

import java.util.UUID

data class CustomAdvancements(
    val oniAdvancementPlayers: MutableList<UUID> = mutableListOf(),
    val dontStarveAdvancementPlayers: MutableList<UUID> = mutableListOf(),
    val factorioAdvancementPlayers: MutableList<UUID> = mutableListOf(),
)
