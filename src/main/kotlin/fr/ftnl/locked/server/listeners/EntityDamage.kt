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
        oniAchievement(event)
    }
    
    
    fun oniAchievement(event: EntityDamageEvent) {
        if (locked.config.easterEggs.customAdvancement.enableOniAdvancement.not()) return
        if (event.entity !is Player)return
        val player = event.entity as Player
        val causes = listOf(
            EntityDamageEvent.DamageCause.DROWNING,
            EntityDamageEvent.DamageCause.SUFFOCATION,
        )
        val advancementPlayers = locked.pData.customAdvancements.oniAdvancementPlayers
        if (event.cause in causes && advancementPlayers.contains(player.uniqueId).not()) {
            advancementPlayers.add(player.uniqueId)
            player.sendMessage("§6Succès débloqué : §f[§cOxygen Not Included§f]")
            player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 15F, 1F)
        }
    }
    
}
