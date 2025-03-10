package fr.ftnl.locked.config.configElements.twitch

import fr.ftnl.locked.config.configElements.twitch.bits.BitsBorderConfig
import fr.ftnl.locked.config.configElements.twitch.bits.ItemSound
import fr.ftnl.locked.config.configElements.twitch.bits.WeatherConfig
import fr.ftnl.locked.config.configElements.twitch.follow.FollowBorderConfig
import fr.ftnl.locked.config.configElements.twitch.hype.HypeTrainConfig
import fr.ftnl.locked.config.configElements.twitch.raid.ChickenRaid
import fr.ftnl.locked.config.configElements.twitch.sub.Cakes
import fr.ftnl.locked.config.configElements.twitch.sub.Poops
import fr.ftnl.locked.config.configElements.twitch.sub.SubBorderConfig

data class TwitchConfig(
    val enableTwitchIntegration: Boolean = false,
    val credentials: TwitchCredentials = TwitchCredentials(),
    val cakes: Cakes = Cakes(), val poops: Poops = Poops(),
    val chickenRaid: ChickenRaid = ChickenRaid(),
    val itemSound: ItemSound = ItemSound(),
    val followBorderConfig: FollowBorderConfig = FollowBorderConfig(),
    val subBorderConfig: SubBorderConfig = SubBorderConfig(),
    val bitsBorderConfig: BitsBorderConfig = BitsBorderConfig(),
    val weatherConfig: WeatherConfig = WeatherConfig(),
    val hypeTrainConfig: HypeTrainConfig = HypeTrainConfig(),
)