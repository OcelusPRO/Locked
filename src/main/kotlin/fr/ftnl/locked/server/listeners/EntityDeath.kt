package fr.ftnl.locked.server.listeners

import fr.ftnl.locked.Locked
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.persistence.PersistentDataType

class EntityDeath(val locked: Locked) : Listener {
    
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        
        if (event.entity.killer == null) return
        
        when (event.entity.type) {
            EntityType.ZOMBIE -> onZombieDeath(event)
            
            else -> {}
        }
        
    }
    
    
    fun onZombieDeath(event: EntityDeathEvent) {
        if (locked.config.easterEggs.entityKillEasterEgg.enableZombieEggs.not()) return
        
        val container = event.entity.persistentDataContainer
        val dName = NamespacedKey(locked, "name")
        val dGenre = NamespacedKey(locked, "men")
        
        val men = container.get(dGenre, PersistentDataType.BOOLEAN) ?: return
        val name = container.get(dName, PersistentDataType.STRING) ?: return
        
        val killer = event.entity.killer ?: return
        val conf = locked.config.easterEggs.entityKillEasterEgg
        val message = if (men) conf.zombieMenDeathMessages.random() else conf.zombieWomenDeathMessages.random()
        killer.sendMessage(killer.identity(), Component.text(message.format(name)))
    }
    
}
