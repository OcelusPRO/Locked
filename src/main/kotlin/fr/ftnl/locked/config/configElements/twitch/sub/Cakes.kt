package fr.ftnl.locked.config.configElements.twitch.sub

data class Cakes(
    val enableCake: Boolean = true, val giftNamesForCake: List<String> = listOf(
        "glados", "chell", "portal", "portal", "caroline", "cave", "cake", "johnson", "wheatley", "ratman", "doug", "aperture"
    )
)