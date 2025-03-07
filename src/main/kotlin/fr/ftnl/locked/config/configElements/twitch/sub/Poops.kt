package fr.ftnl.locked.config.configElements.twitch.sub

data class Poops(
    val enablePoop: Boolean = true, val poopNames: List<String> = listOf(
        "isaac", "marie", "cain", "judas", "eve", "sanson", "azazel", "lazare", "eden", "lilith", "apollyon", "bethanie", "jacob", "esau"
    )
)