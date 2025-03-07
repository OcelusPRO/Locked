package fr.ftnl.locked.config.configElements.twitch.bits

data class ItemSound(
    val enableEggSounds: Boolean = false,
    val eggSoundsCoast: Int = 384,
    
    val enableRandomSound: Boolean = true,
    val randomSoundCoast: Int = 404,
    
    val enableTntActivationSound: Boolean = true,
    val tntActivationSoundCoast: Int = 46,
    
    val enableExplosionSound: Boolean = true,
    val explosionSoundCoast: Int = 407,
)