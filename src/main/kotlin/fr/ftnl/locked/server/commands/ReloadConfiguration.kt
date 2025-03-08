package fr.ftnl.locked.server.commands

import fr.ftnl.locked.Locked
import fr.ftnl.locked.config.Configuration
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.WorldCreator
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import java.io.File
import kotlin.math.roundToInt


class ReloadConfiguration(val main: Locked) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        main.config = Configuration.load(File(main.dataFolder, "config.json"))
        sender.sendMessage("La configuration du plugin a été rechargée.")
        sender.sendMessage("§4Attention, certains paramètres peuvent ne pas être pris en compte sans reload/restart le serveur.")
        return true
    }
}