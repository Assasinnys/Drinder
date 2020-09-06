package by.hackathon.drinder

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import by.hackathon.drinder.data.LoginInfo
import by.hackathon.drinder.data.UserInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserManager @Inject constructor(appContext: Context) {
    var loginInfo: LoginInfo? = null
    var userInfo: UserInfo? = null

    private val preferences: SharedPreferences = appContext.getSharedPreferences(PREF_NAME, MODE_PRIVATE)

    fun saveLoginData() {
        preferences.edit().apply {
            putString(ID_KEY, loginInfo?.id)
            putString(LOGIN_KEY, loginInfo?.login)
            putString(PASS_KEY, loginInfo?.pass)
            apply()
        }
    }

    fun getPreviousLoginData(): Pair<String, String> {
        return preferences.getString(LOGIN_KEY, "")!! to preferences.getString(PASS_KEY, "")!!
    }

    fun logout() {
        loginInfo = null
        userInfo = null
    }

    companion object {
        const val PREF_NAME = "drinkers"
        const val ID_KEY = "id"
        const val LOGIN_KEY = "login"
        const val PASS_KEY = "pass"
    }
}