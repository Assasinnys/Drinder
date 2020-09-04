package by.hackathon.drinder.ui.authorization

import androidx.lifecycle.*
import by.hackathon.drinder.UserManager
import by.hackathon.drinder.data.repository.LoginRepository
import by.hackathon.drinder.util.ERR_EMPTY_FIELD
import by.hackathon.drinder.util.ERR_USER_NOT_EXIST
import by.hackathon.drinder.util.NO_ERROR
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * '!!' used because this fields has been validated and can't be null.
 */

@Suppress("MemberVisibilityCanBePrivate")
class LoginViewModel @Inject constructor(
    val userManager: UserManager,
    val repository: LoginRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val loginNavigationPermission = MutableLiveData(false)
    private val loginErrorField = MutableLiveData<Int>(NO_ERROR)
    private val passErrorField = MutableLiveData<Int>(NO_ERROR)

    val loginNavigationPermissionState: LiveData<Boolean> get() = loginNavigationPermission
    val loginErrorFieldState: LiveData<Int> get() = loginErrorField
    val passErrorFieldState: LiveData<Int> get() = passErrorField

    // DI
//    private val userManager by lazy { getApp().userManager }
//    private val repository: LoginRepository by lazy { getApp().repository }

    override fun onStart(owner: LifecycleOwner) {
        loginNavigationPermission.value = false
    }

    fun notifyLoginRequest(login: String?, pass: String?) {
        if (!isValidFields(login, pass)) return

        viewModelScope.launch {
            val loginInfo = repository.login(login!!, pass!!)
            if (loginInfo != null) {
                userManager.loginInfo = loginInfo
                loginNavigationPermission.value = true
            } else {
                loginErrorField.value = ERR_USER_NOT_EXIST
            }
        }
    }

    private fun isValidFields(login: String?, pass: String?): Boolean {
        var isValid = true

        if (login.isNullOrEmpty()) {
            loginErrorField.value = ERR_EMPTY_FIELD
            isValid = false
        } else {
            loginErrorField.value = NO_ERROR
        }

        if (pass.isNullOrEmpty()) {
            passErrorField.value = ERR_EMPTY_FIELD
            isValid = false
        } else {
            passErrorField.value = NO_ERROR
        }

        return isValid
    }
}