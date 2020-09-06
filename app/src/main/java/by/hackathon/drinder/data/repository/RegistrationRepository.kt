package by.hackathon.drinder.data.repository


interface RegistrationRepository {
    suspend fun register(login: String, password: String): Boolean
}
