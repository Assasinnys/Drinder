package by.hackathon.drinder.data.repository

import by.hackathon.drinder.data.LocationInfo

interface MapRepository {
    suspend fun findDrinkers(): List<LocationInfo>
    suspend fun sendLocation(lat: Double, lon: Double): Boolean
    fun getOwnId(): String?
}
