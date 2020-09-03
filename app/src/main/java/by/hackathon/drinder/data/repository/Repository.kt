package by.hackathon.drinder.data.repository

import by.hackathon.drinder.api.ApiImplementation
import by.hackathon.drinder.data.LoginInfo
import by.hackathon.drinder.data.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository : LoginRepository, RegisterRepository, UserDetailRepository {
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
}