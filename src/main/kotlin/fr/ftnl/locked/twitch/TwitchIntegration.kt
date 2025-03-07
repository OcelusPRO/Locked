package fr.ftnl.locked.twitch

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import fr.ftnl.locked.Locked
import fr.ftnl.locked.twitch.listeners.CheerEvent
import fr.ftnl.locked.twitch.listeners.FollowEvent
import fr.ftnl.locked.twitch.listeners.RaidEvent
import fr.ftnl.locked.twitch.listeners.SubscriptionEvent


class TwitchIntegration(val locked: Locked) {
    
    var credential: OAuth2Credential? = if (locked.config.twitchConfig.credentials.accessToken.isNullOrBlank()) null
    else OAuth2Credential("twitch", locked.config.twitchConfig.credentials.accessToken)
    
    
    var twitchClient: TwitchClient = TwitchClientBuilder.builder()
        
        .withClientId(locked.config.twitchConfig.credentials.clientId).withClientSecret(locked.config.twitchConfig.credentials.clientSecret)
        
        .withEnableHelix(true) // API Helix
        .withChatCommandsViaHelix(false)
        
        .withEnableChat(true) // Pour les messages de chat
        .withChatAccount(credential).withDefaultAuthToken(credential)
        
        //.withEnableEventSocket(true) // Pour les événements
        .build()
    
    init {
        val channel = locked.config.twitchConfig.credentials.channel
        if (twitchClient.chat.isChannelJoined(channel).not()) {
            twitchClient.chat.joinChannel(channel) //twitchClient.clientHelper.enableStreamEventListener(channel)
            twitchClient.clientHelper.enableFollowEventListener(channel)
            twitchClient.clientHelper.enableClipEventListener(channel) // quand un clip est fait faire une action sur le jeu
        }
        
        twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java).registerListener(
                listOf(
                    CheerEvent(locked), FollowEvent(locked), RaidEvent(locked), SubscriptionEvent(locked)
                )
            )
        
        twitchClient.chat.sendMessage(channel, "Plugin minecraft LOCKED chargé !")
    }
}

