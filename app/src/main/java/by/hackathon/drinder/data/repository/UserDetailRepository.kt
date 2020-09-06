package by.hackathon.drinder.data.repository

import by.hackathon.drinder.data.UserInfo

interface UserDetailRepository {
    suspend fun getUserDetail(id: String): UserInfo?
    suspend fun postUserDetail(gender: String, age: Int, alcohol: String, userName: String): Boolean
    fun getSavedUserDetails(): UserInfo?
}
