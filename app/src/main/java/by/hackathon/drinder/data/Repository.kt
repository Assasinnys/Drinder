package by.hackathon.drinder.data

import by.hackathon.drinder.api.ApiImplementation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository : LoginRepository {
    override suspend fun login(login: String, pass: String): LoginInfo? {
        return withContext(Dispatchers.IO) {
            ApiImplementation.login(login, pass)
        }
    }
}