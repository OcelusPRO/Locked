package fr.ftnl.locked.server.commands

import fr.ftnl.locked.Locked
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
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
        val newWorld = createWorld(worldName) ?: return false.also { sender.sendMessage("Erreur lors de la création du monde") }
        val l = generateNewLocation(newWorld)
        
        main.borderSizeManager.setBorderLocation(l, newWorld.name)
        main.borderSizeManager.resetBorder(newWorld.name)
        
        main.commandSender.executeCommandInWorld(newWorld.name, "spawnpoint @a ${l.x.roundToInt()} ${l.y.roundToInt()} ${l.z.roundToInt()}")
        main.commandSender.executeCommand("advancement revoke @a everything")
        main.commandSender.executeCommandInWorld(newWorld.name, "clear @a")
        main.commandSender.executeCommandInWorld(newWorld.name, "experience set @a 0 points")
        main.commandSender.executeCommandInWorld(newWorld.name, "experience set @a 0 levels")
        
        main.server.onlinePlayers.forEach { p ->
            p.health = 20.0
            p.foodLevel = 20
            p.saturation = 10f
            p.teleport(l)
        }
        
        removeOldWorld()
        
        main.pData.reset(main.config)
        main.pData.currentWorldName = newWorld.name
        main.pData.save(File(main.dataFolder, "data.json"))
        main.scoreboardManager.updateScoreboard()
        
        return true
    }

    fun createWorld(worldName: String): World? {
        val worldCreator = WorldCreator(worldName)
        return main.server.createWorld(worldCreator)
    }
    
    fun removeOldWorld(){
        val worldName = main.pData.currentWorldName
        val world = Bukkit.getWorld(worldName) ?: return
        main.server.unloadWorld(world, false)
        val worldFolder = File(main.server.worldContainer, worldName)
        if (worldFolder.exists()) {
            worldFolder.deleteRecursively()
            main.commandSender.executeCommand("say Le monde $worldName a été supprimé")
        }
    }
    
    fun generateNewLocation(world: World): Location{
        val spl = world.spawnLocation
        val illegalRange = -1000..1000
        val range = -4000..4000
        
        var x = range.random()
        while (x in illegalRange) { x = range.random() }
        var z = range.random()
        while (z in illegalRange) { z = range.random() }
        
        var newSpl = Location(spl.world, spl.x + x, spl.y, spl.z  + z)
        val top = world.getHighestBlockAt(newSpl)
        newSpl = top.location.add(0.0, 1.0, 0.0)
        
        for (i in 1..5) if (newSpl.clone().add(0.0, i.toDouble(), 0.0).block.isEmpty.not()) return generateNewLocation(world)
        
        return  newSpl
    }
}
