package by.hackathon.drinder.data.repository

import by.hackathon.drinder.UserManager
import by.hackathon.drinder.api.ApiImplementation
import by.hackathon.drinder.data.LocationInfo
import by.hackathon.drinder.data.LoginInfo
import by.hackathon.drinder.data.Storage
import by.hackathon.drinder.data.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("MemberVisibilityCanBePrivate")
@Singleton
class MainRepository @Inject constructor(
    val apiImplementation: ApiImplementation,
    val storage: Storage,
    val userManager: UserManager
) : LoginRepository,
    RegistrationRepository, UserDetailRepository, MapRepository {

    override suspend fun login(login: String, pass: String): Boolean {
        return withContext(Dispatchers.IO) {
            saveCheckLoginInfo(apiImplementation.login(login, pass))
        }
    }

    override fun logoutUser() {
        userManager.logout()
    }

    private fun saveCheckLoginInfo(loginInfo: LoginInfo?): Boolean {
        return if (loginInfo != null) {
            userManager.login(loginInfo)
            storage.saveLoginData(loginInfo.login, loginInfo.pass)
            true
        } else false
    }

    override fun getPreviousLoginData() = storage.getPreviousLoginData()

    override suspend fun register(login: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            saveCheckLoginInfo(apiImplementation.register(login, password))
        }
    }

    override suspend fun getUserDetail(id: String): UserInfo? {
        return withContext(Dispatchers.IO) {
            apiImplementation.getUserDetail(id)
        }
    }

    override suspend fun getOwnUserDetail(): UserInfo? {
        return userManager.loginInfo?.let {
            getUserDetail(it.id)?.also { ownInfo ->
                userManager.userInfo = ownInfo
            }
        }
    }

    override fun getOwnId(): String? {
        return userManager.loginInfo?.id
    }

    override suspend fun postUserDetail(
        gender: String,
        age: Int,
        alcohol: String,
        userName: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            userManager.loginInfo?.let {
                apiImplementation.postUserDetail(it.login, it.pass, gender, age, alcohol, userName)
                    .also {
                        userManager.userInfo = UserInfo(alcohol, gender, age, userName)
                    }
            } ?: false
        }
    }

    override fun getSavedUserDetails() = userManager.userInfo

    override suspend fun findDrinkers(): List<LocationInfo> {
        return withContext(Dispatchers.IO) {
            userManager.loginInfo?.let {
                apiImplementation.findDrinkers(it.id)
            } ?: emptyList()
        }
    }

    override suspend fun sendLocation(lat: Double, lon: Double): Boolean {
        return withContext(Dispatchers.IO) {
            userManager.loginInfo?.let {
                apiImplementation.sendLocation(it.id, lat, lon)
            } ?: false
        }
    }
}
