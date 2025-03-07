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


/*


        Bukkit.getScheduler().runTask(main, Runnable {
            if (newBorderSize > main.config.borderConfig.minBorderSize) {
                val r = main.config.borderConfig.currentBorder % saveBorderStep
                val currentStep = main.config.borderConfig.currentBorder - r
                if (main.config.borderConfig.minBorderSize != currentStep){
                    main.config.borderConfig.minBorderSize = currentStep
                    main.server.dispatchCommand(Bukkit.getConsoleSender(), "say ${main.config.strings.newStep}")
                    main.server.dispatchCommand(Bukkit.getConsoleSender(), String.format("say ${main.config.strings.newStepDesc}", main.config.borderConfig.minBorderSize))
                    val title = Title.title(
                        Component.text(main.config.strings.newStep)
                            .style(
                                Style.style(TextColor.color(255, 255, 0)) // Jaune.decoration(TextDecoration.BOLD, true)
                            ),
                        Component.text(String.format(main.config.strings.newStepDesc, main.config.borderConfig.minBorderSize)),
                        Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(3), Duration.ofSeconds(1))
                    )
                    
                    main.server.onlinePlayers.filter { it.gameMode == GameMode.SURVIVAL }.forEach {
                        it.showTitle(title)
                        it.playSound(it.location, Sound.ENTITY_PLAYER_LEVELUP, 5f, 1f)
                        
                    }
                }
            }
            main.server.dispatchCommand(Bukkit.getConsoleSender(), "worldborder set $newBorderSize")
        })


* */