package by.hackathon.drinder.data.repository

import by.hackathon.drinder.api.ApiImplementation
import by.hackathon.drinder.data.LoginInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository : LoginRepository, RegisterRepository {
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
}