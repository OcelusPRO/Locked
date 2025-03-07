package fr.ftnl.locked.server.listeners

import fr.ftnl.locked.Locked
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeath(val locked: Locked) : Listener {
    
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        locked.borderSizeManager.setBorder(locked.config.borderConfig.defaultBorder, event.player.world.name)
    }
    
}