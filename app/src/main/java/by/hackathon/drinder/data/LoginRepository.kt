package by.hackathon.drinder.data

interface LoginRepository {
    suspend fun login(login: String, pass: String): LoginInfo?
}