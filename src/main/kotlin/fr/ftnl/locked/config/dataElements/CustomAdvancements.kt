package fr.ftnl.locked.config.dataElements

import java.util.UUID

data class CustomAdvancements(
    val oniAdvancementPlayers: MutableList<UUID> = mutableListOf()
)
