package by.hackathon.drinder

import by.hackathon.drinder.data.LoginInfo
import by.hackathon.drinder.data.UserInfo
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("MemberVisibilityCanBePrivate")
@Singleton
class UserManager @Inject constructor() {
    var loginInfo: LoginInfo? = null
    var userInfo: UserInfo? = null

    fun login(loginInfo: LoginInfo) {
        this.loginInfo = loginInfo
    }

    fun logout() {
        loginInfo = null
        userInfo = null
    }
}
