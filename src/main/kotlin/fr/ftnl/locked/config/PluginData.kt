package fr.ftnl.locked.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.ftnl.locked.config.dataElements.BorderData
import java.io.File

data class PluginData(
    val borderConfig: BorderData = BorderData(), var firstConnect: Boolean = false, var currentWorldName: String = ""
) {
    companion object {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        fun load(file: File): PluginData {
            if (!file.parentFile.exists()) file.parentFile.mkdirs()
            if (file.createNewFile()) {
                val pluginData = PluginData()
                file.writeText(gson.toJson(pluginData))
                pluginData
            }
            return try {
                val dta = gson.fromJson(file.readText(), PluginData::class.java)
                file.writeText(gson.toJson(dta))
                dta
            } catch (e: Exception) {
                val exc = ConfigurationException("Le fichier de donnée n'a pas pu être lu, utilisation de la configuration par default", e)
                println(exc.printStackTrace())
                PluginData()
            }
        }
    }
    
    fun save(file: File) {
        file.writeText(gson.toJson(this))
    }
}
