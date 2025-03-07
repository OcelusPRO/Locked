package fr.ftnl.locked.config.configElements.twitch

data class TwitchCredentials(
    val clientId: String = "", val clientSecret: String = "", val accessToken: String = "", val channel: String = ""
)
