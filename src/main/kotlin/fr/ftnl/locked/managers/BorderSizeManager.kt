package fr.ftnl.locked.managers

import fr.ftnl.locked.Locked
import org.bukkit.Location
import kotlin.math.max

class BorderSizeManager(val main: Locked) {
    
    fun resetBorder(world: String) {
        main.pData.borderConfig.borderSize = main.config.borderConfig.defaultBorder
        main.pData.borderConfig.minBorderSize = main.config.borderConfig.defaultMinBorderSize
        main.commandSender.executeCommandInWorld(world, "worldborder set ${main.config.borderConfig.defaultBorder}")
    }
    
    fun setBorder(size: Double, world: String) {
        main.pData.borderConfig.borderSize = max(size, main.pData.borderConfig.minBorderSize)
        main.commandSender.executeCommandInWorld(world, "worldborder set ${main.pData.borderConfig.borderSize}")
        main.scoreboardManager.updateScoreboard()
    }
    
    fun increaseBorder(size: Double, world: String) {
        main.pData.borderConfig.borderSize += size
        setBorder(main.pData.borderConfig.borderSize, world)
    }
    
    fun setBorderLocation(location: Location, world: String) {
        main.commandSender.executeCommandInWorld(world, "worldborder center ${location.x} ${location.z}")
    }
    
}