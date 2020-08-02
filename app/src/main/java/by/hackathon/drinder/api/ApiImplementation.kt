package by.hackathon.drinder.api

import by.hackathon.drinder.data.LocationInfo
import by.hackathon.drinder.data.LoginInfo
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiImplementation {
    private const val BASE_URL = "https://hackaton-web-server.herokuapp.com"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val service = retrofit.create(DrinderApi::class.java)

    suspend fun findDrinkers(id: String): List<LocationInfo> {
        val response = service.findDrinkers(id)
        return if (response.isSuccessful) response.body()!! else emptyList<LocationInfo>()
    }

    suspend fun sendLocation(id: String, lat: Double, lon: Double): Boolean {
        val response = service.sendLocation(lon.toString(), id, lat.toString())
        return response.isSuccessful
    }

    suspend fun login(login: String, password: String): LoginInfo? {
        val response = service.login(login, password)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun register(login: String, password: String): LoginInfo? {
        val response = service.register(login, password)
        return if (response.isSuccessful) response.body() else null
    }
}