package fr.ftnl.locked.server.listeners

import fr.ftnl.locked.Locked
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoin(val locked: Locked) : Listener {
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        
        if (event.player.world.name != locked.pData.currentWorldName) {
            val world = event.player.server.getWorld(locked.pData.currentWorldName)
                ?: return
            event.player.teleport(world.spawnLocation)
        }
        
        if (locked.pData.firstConnect.not()) {
            locked.pData.firstConnect = true
            locked.borderSizeManager.setBorderLocation(event.player.location, event.player.world.name)
            locked.borderSizeManager.increaseBorder(0.0, event.player.world.name)
        }
        
        locked.scoreboardManager.updateScoreboard()
    }
    
}