package fr.ftnl.locked.config.configElements.easterEggs

data class EasterEgg(
    val entityKillEasterEgg: EntityEasterEggs = EntityEasterEggs(),
    val customAdvancement: CustomAdvancement = CustomAdvancement(),
)
