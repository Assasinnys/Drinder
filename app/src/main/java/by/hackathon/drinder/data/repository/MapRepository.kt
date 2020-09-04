package by.hackathon.drinder.data.repository

import by.hackathon.drinder.data.LocationInfo

interface MapRepository {
    suspend fun findDrinkers(id: String): List<LocationInfo>
    suspend fun sendLocation(id: String, lat: Double, lon: Double): Boolean
}