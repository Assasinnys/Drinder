package by.hackathon.drinder.api

import by.hackathon.drinder.data.LocationInfo
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
}