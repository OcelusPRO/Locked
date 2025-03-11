package fr.ftnl.locked.server.listeners

import fr.ftnl.locked.Locked
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAdvancementDoneEvent

class PlayerAdvancementDown(val locked: Locked) : Listener {
    
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    fun onPlayerAdvancementDoneEvent(event: PlayerAdvancementDoneEvent) {
        if (locked.config.excludedAdvancement.any { event.advancement.key.asString().contains(it) }) return
        val newMinSize = locked.config.borderConfig.defaultMinBorderSize + locked.advancementManager.findAllPlayerAdvancements(event.player).size
        locked.pData.borderConfig.minBorderSize = newMinSize
        locked.scoreboardManager.updateScoreboard()
        locked.borderSizeManager.increaseBorder(locked.config.borderConfig.advancementSizeAddition, event.player.world.name, "advancement")
    }
    
}