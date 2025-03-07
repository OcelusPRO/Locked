package fr.ftnl.locked.managers

import fr.ftnl.locked.Locked
import kotlin.math.roundToLong
import kotlin.time.Duration

class ServerCommandManager(val locked: Locked) {
    fun executeCommand(command: String) = locked.server.scheduler.runTask(locked, Runnable {
        locked.server.dispatchCommand(locked.server.consoleSender, command)
    })
    
    fun executeCommandLater(command: String, delay: Duration) = locked.server.scheduler.runTaskLater(
        locked, Runnable {
            locked.server.dispatchCommand(locked.server.consoleSender, command)
        }, (delay.inWholeSeconds * locked.server.tps.first().roundToLong()) // temps en seconde selon TPS du serveur
    )
    
    
    fun executeCommandInWorld(worldName: String, command: String) = executeCommand("execute in $worldName run $command")
    fun executeCommandOnWorldLater(worldName: String, command: String, delay: Duration) = executeCommandLater("execute in $worldName run $command", delay)
}
