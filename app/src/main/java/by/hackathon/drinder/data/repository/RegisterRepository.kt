package by.hackathon.drinder.data.repository

import by.hackathon.drinder.data.LoginInfo

interface RegisterRepository {
    suspend fun register(login: String, password: String): LoginInfo?
}