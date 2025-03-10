package fr.ftnl.locked.twitch.listeners

import com.github.philippheuer.events4j.simple.domain.EventSubscriber
import com.github.twitch4j.eventsub.events.HypeTrainBeginEvent
import com.github.twitch4j.pubsub.domain.HypeTrainApproaching
import fr.ftnl.locked.Locked
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound

class AppHypeTrainEvent(val locked: Locked) {
    
    
    @EventSubscriber
    fun onHypeStart(event: HypeTrainBeginEvent) {
        if (locked.config.twitchConfig.hypeTrainConfig.enableHypeTrain.not()) return
        locked.server.onlinePlayers.filter { it.gameMode == GameMode.SURVIVAL }.forEach { player ->
            Bukkit.getScheduler().runTask(locked, Runnable {
                val minecart = player.world.spawnEntity(player.location, org.bukkit.entity.EntityType.MINECART)
                minecart.customName(Component.text("Hype Train"))
                minecart.isCustomNameVisible = true
                minecart.velocity = player.location.direction.multiply(0.5)
                
                val title = Component.text("Hype Train !").color(NamedTextColor.GOLD)
                val subtitle = Component.text("§6Départ imminent, veuillez monté dans le train!").color(NamedTextColor.GOLD)
                
                val showTitle = Title.title(title, subtitle)
                player.showTitle(showTitle)
                
                player.sendMessage("§6Veuillez monté dans le train de la hype !")
                minecart.addPassenger(player)
            })
        }
    }
    
    @EventSubscriber
    fun onHypeApproaching(event: HypeTrainApproaching) {
        if (locked.config.twitchConfig.hypeTrainConfig.enableHypeTrain.not()) return
        Bukkit.getScheduler().runTask(locked, Runnable {
            locked.server.onlinePlayers.forEach { player ->
                player.playSound(player.location, Sound.ENTITY_MINECART_RIDING, 15f, 1f)
            }
        })
    }
    
    
    
    
}