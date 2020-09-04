package by.hackathon.drinder.data.repository

import by.hackathon.drinder.api.ApiImplementation
import by.hackathon.drinder.data.LocationInfo
import by.hackathon.drinder.data.LoginInfo
import by.hackathon.drinder.data.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repository @Inject constructor() : LoginRepository, RegistrationRepository, UserDetailRepository, MapRepository {
    override suspend fun login(login: String, pass: String): LoginInfo? {
        return withContext(Dispatchers.IO) {
            ApiImplementation.login(login, pass)
        }
    }

    override suspend fun register(login: String, password: String): LoginInfo? {
        return withContext(Dispatchers.IO) {
            ApiImplementation.register(login, password)
        }
    }

    override suspend fun getUserDetail(id: String): UserInfo? {
        return withContext(Dispatchers.IO) {
            ApiImplementation.getUserDetail(id)
        }
    }

    override suspend fun postUserDetail(
        login: String,
        password: String,
        gender: String,
        age: Int,
        alcohol: String,
        userName: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            ApiImplementation.postUserDetail(login, password, gender, age, alcohol, userName)
        }
    }

    override suspend fun findDrinkers(id: String): List<LocationInfo> {
        return withContext(Dispatchers.IO) {
            ApiImplementation.findDrinkers(id)
        }
    }

    override suspend fun sendLocation(id: String, lat: Double, lon: Double): Boolean {
        return withContext(Dispatchers.IO) {
            ApiImplementation.sendLocation(id, lat, lon)
        }
    }
}