package fr.ftnl.locked.managers

import fr.ftnl.locked.Locked
import org.bukkit.advancement.Advancement
import org.bukkit.entity.Player

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
    
}
