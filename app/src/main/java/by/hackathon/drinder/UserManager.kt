package by.hackathon.drinder

import by.hackathon.drinder.data.LoginInfo
import by.hackathon.drinder.data.Storage
import by.hackathon.drinder.data.UserInfo
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("MemberVisibilityCanBePrivate")
@Singleton
class UserManager @Inject constructor(val storage: Storage) {
    var loginInfo: LoginInfo? = null
    var userInfo: UserInfo? = null

    fun getPreviousLoginData() = storage.getPreviousLoginData()

    fun logout() {
        loginInfo = null
        userInfo = null
    }

    companion object {
        const val LOGIN_KEY = "login"
        const val PASS_KEY = "pass"
    }
}
