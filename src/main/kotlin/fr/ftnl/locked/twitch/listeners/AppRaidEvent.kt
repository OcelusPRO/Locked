package fr.ftnl.locked.twitch.listeners

import com.github.philippheuer.events4j.simple.domain.EventSubscriber
import com.github.twitch4j.chat.events.channel.RaidEvent
import fr.ftnl.locked.Locked
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.EntityType
import kotlin.math.min

class AppRaidEvent(val locked: Locked) {
    
    @EventSubscriber
    fun onRaid(event: RaidEvent) {
        if (locked.config.twitchConfig.chickenRaid.enableRaidChicken.not()) return
        
        Bukkit.getScheduler().runTask(locked, Runnable {
            
            locked.server.onlinePlayers.filter { it.gameMode == GameMode.SURVIVAL }.forEach {
                for (ignored in 1..min(event.viewers, locked.config.twitchConfig.chickenRaid.maxRaidChickens)) {
                    val e = it.world.spawnEntity(it.location, EntityType.CHICKEN)
                    e.customName = "§6Raid Chicken de§r §4§l${event.raider.name}§r"
                }
            }
            
            
        })
    }
    
}