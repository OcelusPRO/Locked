package fr.ftnl.locked

import fr.ftnl.locked.config.Configuration
import fr.ftnl.locked.config.PluginData
import fr.ftnl.locked.managers.AdvancementManager
import fr.ftnl.locked.managers.BorderSizeManager
import fr.ftnl.locked.managers.ScoreboardManager
import fr.ftnl.locked.managers.ServerCommandManager
import fr.ftnl.locked.server.commands.NewWorldCommand
import fr.ftnl.locked.server.listeners.PlayerAdvancementDown
import fr.ftnl.locked.server.listeners.PlayerDeath
import fr.ftnl.locked.server.listeners.PlayerJoin
import fr.ftnl.locked.twitch.TwitchIntegration
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.io.File


class Locked : JavaPlugin() {
    val config: Configuration = Configuration.load(File(dataFolder, "config.json"))
    val pData: PluginData = PluginData.load(File(dataFolder, "data.json"))
    val borderSizeManager: BorderSizeManager = BorderSizeManager(this)
    val commandSender = ServerCommandManager(this)
    val advancementManager = AdvancementManager(this)
    val scoreboardManager by lazy { ScoreboardManager(this) }
    
    var twitchIntegration: TwitchIntegration? = null
    
    override fun onEnable() {
        registerEvents()
        registerCommands()
        
        if (config.twitchConfig.enableTwitchIntegration) twitchIntegration = TwitchIntegration(this)
    }
    
    override fun onDisable() {
        super.onDisable()
        
        pData.save(File(dataFolder, "data.json"))
        
        twitchIntegration?.twitchClient?.eventManager?.close()
        twitchIntegration?.twitchClient?.close()
    }
    
    
    fun registerEvents() {
        Bukkit.getPluginManager().registerEvents(PlayerAdvancementDown(this), this)
        Bukkit.getPluginManager().registerEvents(PlayerDeath(this), this)
        Bukkit.getPluginManager().registerEvents(PlayerJoin(this), this)
    }
    
    fun registerCommands() {
        this.getCommand("reset")?.setExecutor(NewWorldCommand(this))
    }
    
}