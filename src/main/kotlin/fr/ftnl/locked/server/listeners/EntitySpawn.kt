package fr.ftnl.locked.server.listeners

import fr.ftnl.locked.Locked
import net.kyori.adventure.text.Component
import org.bukkit.NamespacedKey
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.MetadataValue
import org.bukkit.persistence.PersistentDataType
import kotlin.random.Random

class EntitySpawn(val locked: Locked) : Listener {
    
    @EventHandler
    fun onEntityDeath(event: EntitySpawnEvent) {
        
        when (event.entity.type) {
            EntityType.ZOMBIE -> onZombieSpawn(event)
            
            else -> {}
        }
        
    }
    
    
    fun onZombieSpawn(event: EntitySpawnEvent) {
        if (locked.config.easterEggs.entityKillEasterEgg.enableZombieEggs.not()) return
        
        if (Random.nextInt(0, 100) > locked.config.easterEggs.entityKillEasterEgg.specialZombieChances) return
        val men = Random.nextBoolean()
        
        val name = if (men) locked.config.easterEggs.entityKillEasterEgg.zombieMenNames.random()
        else locked.config.easterEggs.entityKillEasterEgg.zombieWomenNames.random()
        
        val container = event.entity.persistentDataContainer
        
        val dName = NamespacedKey(locked, "name")
        val dGenre = NamespacedKey(locked, "men")
        val dSpecial = NamespacedKey(locked, "special")
        container.set(dName, PersistentDataType.STRING, name)
        container.set(dGenre, PersistentDataType.BOOLEAN, men)
        container.set(dSpecial, PersistentDataType.BOOLEAN, true)
        
        
        event.entity.customName(Component.text(name))
        event.entity.isCustomNameVisible = true
    }
    
}
