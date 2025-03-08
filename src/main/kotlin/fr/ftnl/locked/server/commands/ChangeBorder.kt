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


class ChangeBorder(val main: Locked) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("§4Usage: /changeborder <size>")
            return false
        }
        
        val size = args[0].toDoubleOrNull() ?: return false.also { sender.sendMessage("§4Usage: /changeborder <size>") }
        
        main.borderSizeManager.increaseBorder(size, main.pData.currentWorldName)
        return true
    }
}