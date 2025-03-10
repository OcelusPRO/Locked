package fr.ftnl.locked.managers

import fr.ftnl.locked.Locked
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import kotlin.math.roundToInt

class ScoreboardManager(val locked: Locked) {
    
    private val uiConfig = locked.config.uiConfig
    lateinit var board: Scoreboard
    private val showScoreboard = uiConfig.showMinWorldborderScoreboard or uiConfig.showCurrentWorldborderScoreboard
    lateinit var objective: Objective
    
    init {
        Bukkit.getScheduler().runTask(locked, Runnable {
            board = locked.server.scoreboardManager.newScoreboard
            objective = board.getObjective("locked")
                ?: board.registerNewObjective("locked", Criteria.DUMMY, Component.text("§k....§r §l§6Locked §r§k....§r"))
        })
    }
    
    fun updateScoreboard() {
        Bukkit.getScheduler().runTask(locked, Runnable {

            
            if (!showScoreboard) objective.displaySlot = null else objective.displaySlot = DisplaySlot.SIDEBAR
            if (uiConfig.showCurrentWorldborderScoreboard) objective.getScore("Worldborder").score = locked.pData.borderConfig.borderSize.roundToInt()
            if (uiConfig.showMinWorldborderScoreboard) objective.getScore("Min Worldborder").score = locked.pData.borderConfig.minBorderSize.roundToInt()
            if (showScoreboard) locked.server.onlinePlayers.forEach { it.scoreboard = board }
        })
    }
    
}