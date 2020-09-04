package by.hackathon.drinder.data.repository

import by.hackathon.drinder.data.LoginInfo

interface RegistrationRepository {
    suspend fun register(login: String, password: String): LoginInfo?
}