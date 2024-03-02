package com.example.greencropapp.api


data class MessageResponse(
    val message: String
)

data class Sign_inResponse(
    val user_id: Int,
    val message: String
)

data class SensorsResponse(
    val air_humidity: Int,
    val soil_moisture: Int,
    val temperature: Int,
    val light: Int,
    val sensor_co2: Int,
    val water_level: Int,
)

data class GreenHousesResponse(
    val pk: Int,
    val name: String,
    val imei: String,
    val location: String,
    val ventilation: String,
    val watering: String,
    val light: String,
)