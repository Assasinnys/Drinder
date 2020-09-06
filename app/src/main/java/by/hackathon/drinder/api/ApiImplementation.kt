package by.hackathon.drinder.api

import by.hackathon.drinder.data.LocationInfo
import by.hackathon.drinder.data.LoginInfo
import by.hackathon.drinder.data.UserInfo
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiImplementation @Inject constructor() {
    private val baseUrl = "https://hackaton-web-server.herokuapp.com"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(baseUrl)
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

    suspend fun getUserDetail(id: String): UserInfo? {
        val response = service.getUserDetail(id)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun postUserDetail(login: String, password: String, gender: String, age: Int, alcohol: String, userName: String): Boolean {
        val response = service.postUserDetail(login, password, alcohol, gender, age, userName)
        return response.isSuccessful
    }
}