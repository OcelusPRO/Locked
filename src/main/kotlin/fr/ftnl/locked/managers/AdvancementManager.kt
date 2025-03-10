package fr.ftnl.locked.managers

import fr.ftnl.locked.Locked
import fr.ftnl.locked.twitch.listeners.AppFollowEvent
import org.bukkit.Sound
import org.bukkit.advancement.Advancement
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.CraftItemEvent

class AdvancementManager(val locked: Locked) {
    
    private val allAdvancements: List<Advancement> = findAllAdvancements()
    
    private fun findAllAdvancements(): List<Advancement> {
        val advancements = mutableListOf<Advancement>()
        locked.server.advancementIterator().forEach { advancement ->
            advancements.add(advancement)
        }
        return advancements.filter { a -> locked.config.excludedAdvancement.none { a.key.toString().contains(it) } }
    }
    
    fun findAllPlayerAdvancements(player: Player): List<Advancement> = allAdvancements.filter { player.getAdvancementProgress(it).isDone }
    
    fun findAllPlayerMissingAdvancements(player: Player): List<Advancement> = allAdvancements.filter { player.getAdvancementProgress(it).isDone.not() }

    
/*
* Custom advancements
* */
    
    
    
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
            locked.borderSizeManager.increaseBorder(locked.config.borderConfig.advancementSizeAddition, locked.pData.currentWorldName)
        }
    }
    
    fun dontStarveAchievement(event: EntityDamageEvent) {
        if (locked.config.easterEggs.customAdvancement.enableDontStarveAdvancement.not()) return
        if (event.entity !is Player)return
        val player = event.entity as Player
        val causes = listOf(
            EntityDamageEvent.DamageCause.STARVATION
        )
        val advancementPlayers = locked.pData.customAdvancements.dontStarveAdvancementPlayers
        if (event.cause in causes && advancementPlayers.contains(player.uniqueId).not()) {
            advancementPlayers.add(player.uniqueId)
            player.sendMessage("§6Succès débloqué : §f[§Don't Starve§f]")
            player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 15F, 1F)
            locked.borderSizeManager.increaseBorder(locked.config.borderConfig.advancementSizeAddition, locked.pData.currentWorldName)
        }
    }
    
    fun factorioAchievement(event: CraftItemEvent ) {
        if (locked.config.easterEggs.customAdvancement.enableFactorioAdvancement.not()) return
        val advancementPlayers = locked.pData.customAdvancements.factorioAdvancementPlayers
        val player = event.whoClicked as? Player ?: return
        if (advancementPlayers.contains(player.uniqueId).not()) {
            advancementPlayers.add(player.uniqueId)
            player.sendMessage("§6Succès débloqué : §f[§Factory must grows !§f]")
            player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 15F, 1F)
            locked.borderSizeManager.increaseBorder(locked.config.borderConfig.advancementSizeAddition, locked.pData.currentWorldName)
        }
    }
}
