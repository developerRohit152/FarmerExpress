package com.rns.farmerexpress.model



data class WeatherModel(
    val city : Cities,
    val weather : Weathers,

)

data class Cities(
    val locale : Locales,
)

data class Locales(
    val locale2 : String,
    val locale3 : String,
    val locale4 : String,
)

data class Weathers(
    val cloudCoverPhrase : String,
    val dayOfWeek : String,
    val dayOrNight : String,
    val relativeHumidity : String,
    val temperature : String,
    val temperatureFeelsLike : String,
    val temperatureMax24Hour : String,
    val temperatureMin24Hour : String,
    val windSpeed : String,
    val wxPhraseLong : String,

)





