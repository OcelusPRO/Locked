package fr.ftnl.locked.config.configElements.twitch.bits

data class WeatherConfig( // nombres selon chat-gpt
    val enableWeatherChange : Boolean = true, // pression atmospherique moyenne au niveau de la mer = 1013
    val rainCost : List<Int> = listOf(1013, 500), // 500mm = précipitation moyenne annuelle de pluie
    val thunderCost : List<Int> = listOf(1013, 700), // 700hPa = pression atmospherique moyenne pendant orage
    val clearCost : List<Int> = listOf(1013, 750), // 750mmHg = environ 1000hPa = pression atmospherique moyenne pendant temps clair
    val weatherDurationInSeconds : Int = 300, // seconds
)
