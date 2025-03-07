package fr.ftnl.locked.config.configElements.twitch.bits

data class BitsBorderConfig(
    val bitsPart: Double = 100.0, val bitsBorderChanger: Double = 0.2, val minusTriggersWords: List<String> = listOf(
        "moins", "retire", "remove", "-", "min"
    )
)
