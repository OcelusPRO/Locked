package fr.ftnl.locked.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.ftnl.locked.config.configElements.BorderConfig
import fr.ftnl.locked.config.configElements.UiConfig
import fr.ftnl.locked.config.configElements.easterEggs.EasterEgg
import fr.ftnl.locked.config.configElements.twitch.TwitchConfig
import java.io.File

class ConfigurationException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)

data class Configuration(
    val borderConfig: BorderConfig = BorderConfig(),
    val excludedAdvancement: List<String> = listOf("minecraft:recipes/"),
    val twitchConfig: TwitchConfig = TwitchConfig(),
    val soundDelayInSeconds: Long = 0L,
    val uiConfig: UiConfig = UiConfig(),
    val easterEggs: EasterEgg = EasterEgg(),
) {
    companion object {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        fun load(file: File): Configuration {
            if (!file.parentFile.exists()) file.parentFile.mkdirs()
            if (file.createNewFile()) {
                val config = Configuration()
                file.writeText(gson.toJson(config))
                config
            }
            return try {
                val cfg = gson.fromJson(file.readText(), Configuration::class.java)
                file.writeText(gson.toJson(cfg))
                cfg
            } catch (e: Exception) {
                val exc = ConfigurationException("Le fichier de configuration n'a pas pu être lu, utilisation de la configuration par default", e)
                println(exc.printStackTrace())
                Configuration()
            }
        }
    }
    
    fun save(file: File) {
        file.writeText(gson.toJson(this))
    }
}


