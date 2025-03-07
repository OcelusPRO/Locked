package fr.ftnl.locked.server.commands

import fr.ftnl.locked.Locked
import org.bukkit.Bukkit
import org.bukkit.WorldCreator
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.io.File
import kotlin.math.roundToInt


class NewWorldCommand(val main: Locked) : CommandExecutor {
    var count = main.pData.currentWorldName.split("_").last().toIntOrNull()
        ?: 0
    
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        count++
        val worldName = "world_$count"
        
        // send message to sender
        sender.sendMessage("Création du monde $worldName") // Création d'un nouveau monde
        val worldCreator = WorldCreator(worldName)
        val newWorld = main.server.createWorld(worldCreator)
            ?: return false
        
        
        val spl = newWorld.spawnLocation
        
        main.borderSizeManager.setBorderLocation(spl, newWorld.name)
        main.borderSizeManager.resetBorder(newWorld.name)
        
        main.commandSender.executeCommandInWorld(newWorld.name, "spawnpoint @a ${spl.x.roundToInt()} ${spl.y.roundToInt()} ${spl.z.roundToInt()}")
        main.commandSender.executeCommand("advancement revoke @a everything")
        main.commandSender.executeCommandInWorld(newWorld.name, "clear @a")
        main.commandSender.executeCommandInWorld(newWorld.name, "experience set @a 0 points")
        main.commandSender.executeCommandInWorld(newWorld.name, "experience set @a 0 levels")
        for (p in Bukkit.getServer().onlinePlayers) {
            p.health = 20.0
        }
        for (p in Bukkit.getServer().onlinePlayers) {
            p.foodLevel = 20
        }
        for (p in Bukkit.getServer().onlinePlayers) {
            p.saturation = 10f
        }
        main.server.onlinePlayers.forEach { it.teleport(spl) }
        
        main.pData.currentWorldName = newWorld.name
        main.pData.save(File(main.dataFolder, "data.json"))
        main.scoreboardManager.updateScoreboard()
        
        return true
    }
}