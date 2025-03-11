package fr.ftnl.locked.server.listeners

import fr.ftnl.locked.Locked
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.inventory.CraftItemEvent


class CraftItem(val locked: Locked) : Listener {
    
    @EventHandler
    fun onCraftItem(event: CraftItemEvent) {
    }
}
