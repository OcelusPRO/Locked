package fr.ftnl.locked.twitch

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.eventsub.subscriptions.SubscriptionTypes
import fr.ftnl.locked.Locked
import fr.ftnl.locked.twitch.listeners.AppCheerEvent
import fr.ftnl.locked.twitch.listeners.AppFollowEvent
import fr.ftnl.locked.twitch.listeners.AppHypeTrainEvent
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
        val user = twitchClient.helix.getUsers(null, null, listOf(channel)).execute().users.find { it.login == channel }
            ?: throw IllegalStateException("User not found")
        val userId = user.id
        
        if (twitchClient.chat.isChannelJoined(channel).not()) {
            twitchClient.chat.joinChannel(channel)
            twitchClient.clientHelper.enableFollowEventListener(channel)
        }
        
        twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java).registerListener(AppCheerEvent(locked))
        twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java).registerListener(AppFollowEvent(locked))
        twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java).registerListener(AppRaidEvent(locked))
        twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java).registerListener(AppSubscriptionEvent(locked))
        twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java).registerListener(AppHypeTrainEvent(locked))
        
        val eventSocket = twitchClient.getEventSocket()
        
        eventSocket.register(SubscriptionTypes.CHANNEL_FOLLOW_V2.prepareSubscription({ it.broadcasterUserId(userId).build() }, null))
        eventSocket.register(SubscriptionTypes.HYPE_TRAIN_BEGIN.prepareSubscription({ it.broadcasterUserId(userId).build() }, null))
        eventSocket.register(SubscriptionTypes.HYPE_TRAIN_PROGRESS.prepareSubscription({ it.broadcasterUserId(userId).build() }, null))
        //eventSocket.register(SubscriptionTypes.POLL_END.prepareSubscription({ it.broadcasterUserId(userId).build() }, null))
        eventSocket.connect()
        
    
        twitchClient.chat.sendMessage(channel, "Plugin minecraft LOCKED chargé !")
    }
}

