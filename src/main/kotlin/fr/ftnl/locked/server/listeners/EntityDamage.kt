package fr.ftnl.locked.server.listeners

import fr.ftnl.locked.Locked
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent


class EntityDamage(val locked: Locked) : Listener {
    
    
    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        locked.advancementManager.oniAchievement(event)
        locked.advancementManager.dontStarveAchievement(event)
        
    }
    

    
}
