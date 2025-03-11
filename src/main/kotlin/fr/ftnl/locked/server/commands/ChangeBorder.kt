package fr.ftnl.locked.server.commands

import fr.ftnl.locked.Locked
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender


class ChangeBorder(val main: Locked) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("§4Usage: /changeborder <size>")
            return false
        }
        
        val size = args[0].toDoubleOrNull() ?: return false.also { sender.sendMessage("§4Usage: /changeborder <size>") }
        
        main.borderSizeManager.increaseBorder(size, main.pData.currentWorldName, "user command")
        return true
    }
}