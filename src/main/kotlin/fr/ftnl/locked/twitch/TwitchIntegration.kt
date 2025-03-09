package fr.ftnl.locked.twitch

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.events.channel.FollowEvent
import com.github.twitch4j.events.ChannelFollowCountUpdateEvent
import fr.ftnl.locked.Locked
import fr.ftnl.locked.twitch.listeners.AppCheerEvent
import fr.ftnl.locked.twitch.listeners.AppFollowEvent
import fr.ftnl.locked.twitch.listeners.AppRaidEvent
import fr.ftnl.locked.twitch.listeners.AppSubscriptionEvent


class TwitchIntegration(val locked: Locked) {
    
    var credential: OAuth2Credential? = if (locked.config.twitchConfig.credentials.accessToken.isBlank()) null
    else OAuth2Credential("twitch", locked.config.twitchConfig.credentials.accessToken)
    
    
    var twitchClient: TwitchClient = TwitchClientBuilder.builder()
        
        .withClientId(locked.config.twitchConfig.credentials.clientId).withClientSecret(locked.config.twitchConfig.credentials.clientSecret)
        
        .withEnableHelix(true) // API Helix
        .withChatCommandsViaHelix(false)
        
        .withEnableChat(true) // Pour les messages de chat
        .withChatAccount(credential).withDefaultAuthToken(credential)
        
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
                    AppCheerEvent(locked),
                    AppFollowEvent(locked),
                    AppRaidEvent(locked),
                    AppSubscriptionEvent(locked)
                )
            )
        
        twitchClient.eventManager.onEvent(FollowEvent::class.java) { e ->
            println("Follow event fallback : " + e.user.name + " followed the channel " + e.channel.name)
        }
        twitchClient.eventManager.onEvent(ChannelFollowCountUpdateEvent::class.java) { e ->
            println("FollowCount event fallback : " + e.followCount + " " + e.channel.name)
        }
        
    
        twitchClient.chat.sendMessage(channel, "Plugin minecraft LOCKED chargé !")
    }
}

