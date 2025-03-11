package fr.ftnl.locked.twitch.listeners

import com.github.philippheuer.events4j.simple.domain.EventSubscriber
import com.github.twitch4j.chat.events.channel.CheerEvent
import fr.ftnl.locked.Locked
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import kotlin.math.abs

class AppCheerEvent(val locked: Locked) {
    
    @EventSubscriber
    fun onCheer(event: CheerEvent) {
        bitsBorderChange(event.bits, event.message)
        bitsSounds(event.bits, event.message)
        bitsWeatherChange(event.bits)
    }
    
    private fun bitsBorderChange(bits: Int, message: String) {
        val bitsPart = abs(bits / locked.config.twitchConfig.bitsBorderConfig.bitsPart)
        if (bitsPart < 1) return
        var changeBorder = bitsPart * locked.config.twitchConfig.bitsBorderConfig.bitsBorderChanger
        if (locked.config.twitchConfig.bitsBorderConfig.minusTriggersWords.any { message.contains(it, true) }) changeBorder = changeBorder * -1
        locked.borderSizeManager.increaseBorder(changeBorder, locked.pData.currentWorldName, "twitch cheers")
    }
    
    private fun bitsSounds(bits: Int, message: String) {
        when (bits) {
            locked.config.twitchConfig.itemSound.eggSoundsCost   -> if (locked.config.twitchConfig.itemSound.enableEggSounds) eggSounds(message)
            locked.config.twitchConfig.itemSound.randomSoundCost        -> if (locked.config.twitchConfig.itemSound.enableRandomSound) randomSounds()
            locked.config.twitchConfig.itemSound.tntActivationSoundCost -> if (locked.config.twitchConfig.itemSound.enableTntActivationSound) playSounds(Sound.ENTITY_TNT_PRIMED)
            locked.config.twitchConfig.itemSound.explosionSoundCost     -> if (locked.config.twitchConfig.itemSound.enableExplosionSound) playSounds(Sound.ENTITY_GENERIC_EXPLODE)
            
        }
    }
    
    private fun eggSounds(message: String) { // extract number from message with regex, number must be separated by space
        val number = message.split(" ").find { it.toIntOrNull() != null }?.toIntOrNull()
            ?: return
        Bukkit.getScheduler().runTaskLater(locked, Runnable {
            locked.server.onlinePlayers.filter { it.gameMode == GameMode.SURVIVAL }.forEach {
                it.playSound(it.location, getSoundFromNumber(number), 14f, 1f)
            }
        }, locked.config.soundDelayInSeconds)
    }
    
    private fun randomSounds() {
        Bukkit.getScheduler().runTaskLater(locked, Runnable {
            locked.server.onlinePlayers.filter { it.gameMode == GameMode.SURVIVAL }.forEach {
                it.playSound(it.location, Sound.entries.random(), 14f, 1f)
            }
        }, locked.config.soundDelayInSeconds)
        
    }
    
    private fun playSounds(sound: Sound) {
        Bukkit.getScheduler().runTaskLater(locked, Runnable {
            locked.server.onlinePlayers.filter { it.gameMode == GameMode.SURVIVAL }.forEach {
                it.playSound(it.location, sound, 14f, 1f)
            }
        }, locked.config.soundDelayInSeconds)
        
    }
    
    private fun getSoundFromNumber(number: Int): Sound {
        return when (number) {
            65   -> Sound.ENTITY_BAT_AMBIENT
            61   -> Sound.ENTITY_BLAZE_SHOOT
            59   -> Sound.ENTITY_SPIDER_AMBIENT
            93   -> Sound.ENTITY_CHICKEN_AMBIENT
            92   -> Sound.ENTITY_COW_AMBIENT
            50   -> Sound.ENTITY_CREEPER_PRIMED
            31   -> Sound.ENTITY_DONKEY_AMBIENT
            4    -> Sound.ENTITY_ELDER_GUARDIAN_AMBIENT
            58   -> Sound.ENTITY_ENDERMAN_TELEPORT
            67   -> Sound.ENTITY_ENDERMITE_AMBIENT
            34   -> Sound.ENTITY_EVOKER_AMBIENT
            56   -> Sound.ENTITY_GHAST_SHOOT
            68   -> Sound.ENTITY_GUARDIAN_ATTACK
            100  -> Sound.ENTITY_HORSE_AMBIENT
            23   -> Sound.ENTITY_HUSK_AMBIENT
            103  -> Sound.ENTITY_LLAMA_AMBIENT
            62   -> Sound.ENTITY_MAGMA_CUBE_JUMP
            96   -> Sound.ENTITY_MOOSHROOM_SHEAR
            32   -> Sound.ENTITY_MULE_AMBIENT
            98   -> Sound.ENTITY_OCELOT_AMBIENT
            90   -> Sound.ENTITY_PIG_STEP
            102  -> Sound.ENTITY_POLAR_BEAR_AMBIENT
            101  -> Sound.ENTITY_RABBIT_AMBIENT
            91   -> Sound.ENTITY_SHEEP_AMBIENT
            69   -> Sound.ENTITY_SHULKER_AMBIENT
            51   -> Sound.ENTITY_SKELETON_SHOOT
            28   -> Sound.ENTITY_SKELETON_HORSE_HURT
            55   -> Sound.ENTITY_SLIME_JUMP
            52   -> Sound.ENTITY_SPIDER_STEP
            94   -> Sound.ENTITY_SQUID_AMBIENT
            6    -> Sound.ENTITY_STRAY_AMBIENT
            35   -> Sound.ENTITY_VEX_AMBIENT
            120  -> Sound.ENTITY_VILLAGER_AMBIENT
            36   -> Sound.ENTITY_VINDICATOR_AMBIENT
            66   -> Sound.ENTITY_WITCH_AMBIENT
            5    -> Sound.ENTITY_WITHER_SKELETON_AMBIENT
            95   -> Sound.ENTITY_WOLF_HOWL
            54   -> Sound.ENTITY_ZOMBIE_AMBIENT
            29   -> Sound.ENTITY_ZOMBIE_HORSE_AMBIENT
            57   -> Sound.ENTITY_ZOMBIFIED_PIGLIN_ANGRY
            27   -> Sound.ENTITY_ZOMBIE_VILLAGER_AMBIENT
            else -> Sound.ENTITY_CREEPER_PRIMED
        }
    }
    
    private fun bitsWeatherChange(bits: Int) {
        if (locked.config.twitchConfig.weatherConfig.enableWeatherChange.not()) return
        val duration = locked.config.twitchConfig.weatherConfig.weatherDurationInSeconds * 20
        
        val actionList = mutableListOf<String>()
        
        when (bits) {
            in locked.config.twitchConfig.weatherConfig.rainCost -> actionList.add("rain")
            in locked.config.twitchConfig.weatherConfig.clearCost -> actionList.add("clear")
            in locked.config.twitchConfig.weatherConfig.thunderCost -> actionList.add("thunder")
        }
        
        if (actionList.isEmpty()) return
        
        locked.server.scheduler.runTask(locked, Runnable {
            when (actionList.randomOrNull()) {
                "rain" -> {
                    Bukkit.getWorld(locked.pData.currentWorldName)?.setStorm(true)
                    Bukkit.getWorld(locked.pData.currentWorldName)?.isThundering = false
                    Bukkit.getWorld(locked.pData.currentWorldName)?.weatherDuration = duration
                }
                "clear" -> {
                    Bukkit.getWorld(locked.pData.currentWorldName)?.setStorm(false)
                    Bukkit.getWorld(locked.pData.currentWorldName)?.isThundering = false
                    Bukkit.getWorld(locked.pData.currentWorldName)?.weatherDuration = duration
                    
                }
                "thunder" -> {
                    Bukkit.getWorld(locked.pData.currentWorldName)?.setStorm(true)
                    Bukkit.getWorld(locked.pData.currentWorldName)?.isThundering = true
                    Bukkit.getWorld(locked.pData.currentWorldName)?.weatherDuration = duration
                }
            }
        })
    }
    
}