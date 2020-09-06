package by.hackathon.drinder.data.repository

interface LoginRepository {
    suspend fun login(login: String, pass: String): Boolean
    fun getPreviousLoginData(): Pair<String, String>
    fun logoutUser()
}
