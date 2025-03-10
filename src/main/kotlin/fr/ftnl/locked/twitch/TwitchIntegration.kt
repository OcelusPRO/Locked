package fr.ftnl.locked.twitch

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.eventsub.subscriptions.SubscriptionTypes
import fr.ftnl.locked.Locked
import fr.ftnl.locked.twitch.listeners.AppCheerEvent
import fr.ftnl.locked.twitch.listeners.AppFollowEvent
import fr.ftnl.locked.twitch.listeners.AppRaidEvent
import fr.ftnl.locked.twitch.listeners.AppSubscriptionEvent


class TwitchIntegration(val locked: Locked) {
    
    var credential: OAuth2Credential? = if (locked.config.twitchConfig.credentials.accessToken.isBlank()) null
    else OAuth2Credential("twitch", locked.config.twitchConfig.credentials.accessToken)
    
    
    var twitchClient: TwitchClient = TwitchClientBuilder.builder()
        .withClientId(locked.config.twitchConfig.credentials.clientId)
        .withClientSecret(locked.config.twitchConfig.credentials.clientSecret)
        .withEnableHelix(true) // API Helix
        .withChatCommandsViaHelix(false)
        .withEnableChat(true) // Pour les messages de chat
        .withChatAccount(credential)
        .withDefaultAuthToken(credential)
        .withEnableEventSocket(true)
        .build()
    
    init {
        val channel = locked.config.twitchConfig.credentials.channel
        val userId = "80857446"
        
        if (twitchClient.chat.isChannelJoined(channel).not()) {
            twitchClient.chat.joinChannel(channel)
            twitchClient.clientHelper.enableFollowEventListener(channel)
        }
        
        twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java).registerListener(AppCheerEvent(locked))
        twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java).registerListener(AppFollowEvent(locked))
        twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java).registerListener(AppRaidEvent(locked))
        twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java).registerListener(AppSubscriptionEvent(locked))
        
        val eventSocket = twitchClient.getEventSocket()
        val subscriptionConfig = SubscriptionTypes.CHANNEL_FOLLOW_V2.prepareSubscription(
            { b ->
                b.broadcasterUserId(userId)
                b.moderatorUserId(userId)
                b.build()
            }, null)
        eventSocket.register(subscriptionConfig)
        eventSocket.connect()
    
        twitchClient.chat.sendMessage(channel, "Plugin minecraft LOCKED chargé !")
    }
}

