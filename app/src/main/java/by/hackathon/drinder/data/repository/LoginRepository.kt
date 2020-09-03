package by.hackathon.drinder.data.repository

import by.hackathon.drinder.data.LoginInfo

interface LoginRepository {
    suspend fun login(login: String, pass: String): LoginInfo?
}