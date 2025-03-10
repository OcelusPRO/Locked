package fr.ftnl.locked.twitch.listeners

import com.github.philippheuer.events4j.simple.domain.EventSubscriber
import com.github.twitch4j.eventsub.events.ChannelFollowEvent
import fr.ftnl.locked.Locked


class AppFollowEvent(val locked: Locked) {
    
    val followList = mutableListOf<String>()
    

    @EventSubscriber
    fun onEventSubFollow(event: ChannelFollowEvent) {
        println("New follow received with EventSub: " + event.userName)
        if (event.userId in followList) return
        followList.add(event.userId)
        locked.borderSizeManager.increaseBorder(
            locked.config.twitchConfig.followBorderConfig.followBorderChanger, locked.pData.currentWorldName
        )
    }
    
    
    
    
}