package fr.ftnl.locked.config.configElements.twitch.bits

data class ItemSound(
    val enableEggSounds: Boolean = false,
    val eggSoundsCost: Int = 384,
    
    val enableRandomSound: Boolean = true,
    val randomSoundCost: Int = 404,
    
    val enableTntActivationSound: Boolean = true,
    val tntActivationSoundCost: Int = 46,
    
    val enableExplosionSound: Boolean = true,
    val explosionSoundCost: Int = 407,
)