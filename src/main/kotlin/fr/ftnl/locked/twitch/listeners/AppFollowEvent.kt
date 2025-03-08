package fr.ftnl.locked.twitch.listeners

import com.github.philippheuer.events4j.simple.domain.EventSubscriber
import com.github.twitch4j.chat.events.channel.FollowEvent
import fr.ftnl.locked.Locked


class AppFollowEvent(val locked: Locked) {
    
    val followList = mutableListOf<String>()
    
    @EventSubscriber
    fun onFollow(event: FollowEvent) {
        println(event.user.name + " followed the channel " + event.channel.name)
        
        if (event.user.id in followList) return
        followList.add(event.user.id)
        locked.borderSizeManager.increaseBorder(
            locked.config.twitchConfig.followBorderConfig.followBorderChanger, locked.pData.currentWorldName
        )
    }
    
    
}