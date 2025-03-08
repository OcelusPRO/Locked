package fr.ftnl.locked.twitch.listeners

import com.github.philippheuer.events4j.simple.domain.EventSubscriber
import com.github.twitch4j.chat.events.channel.GiftSubscriptionsEvent
import com.github.twitch4j.chat.events.channel.SubscriptionEvent
import fr.ftnl.locked.Locked
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class AppSubscriptionEvent(val locked: Locked) {
    
    @EventSubscriber
    fun onSub(event: SubscriptionEvent) = subHandler(event.user.name)
    
    @EventSubscriber
    fun onSubMysteryGift(event: GiftSubscriptionsEvent) = subHandler(event.user.name)
    
    private fun subHandler(name: String) {
        locked.borderSizeManager.increaseBorder(
            locked.config.twitchConfig.subBorderConfig.subBorderChanger, locked.pData.currentWorldName
        )
        if (locked.config.twitchConfig.cakes.giftNamesForCake.any { name.contains(it, true) }) cakeGenerator()
        if (locked.config.twitchConfig.poops.poopNames.any { name.contains(it, true) }) poopGenerator(name)
    }
    
    private fun cakeGenerator() {
        if (locked.config.twitchConfig.cakes.enableCake.not()) return
        Bukkit.getScheduler().runTask(locked, Runnable {
            locked.server.onlinePlayers.filter { it.gameMode == GameMode.SURVIVAL }.forEach { player ->
                val item = ItemStack(Material.CAKE, 1)
                player.world.dropItemNaturally(player.location, item)
            }
        })
    }
    
    private fun poopGenerator(username: String) {
        if (locked.config.twitchConfig.poops.enablePoop.not()) return
        Bukkit.getScheduler().runTask(locked, Runnable {
            locked.server.onlinePlayers.filter { it.gameMode == GameMode.SURVIVAL }.forEach { player ->
                val item = ItemStack(Material.ROTTEN_FLESH, 1)
                val meta = item.itemMeta
                
                meta?.setDisplayName("💩 §4Caca de §6§l$username§r")
                item.itemMeta = meta
                player.world.dropItemNaturally(player.location, item)
            }
        })
    }
    
}