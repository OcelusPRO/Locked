package fr.ftnl.locked.config.configElements

data class BorderConfig(
    val advancementSizeAddition: Double = 1.0, val borderStep: Double = 16.0,
    val logChangeInTchat: Boolean = true,
    val defaultBorder: Double = 16.0, val defaultMinBorderSize: Double = 16.0
)