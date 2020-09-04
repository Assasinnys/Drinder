package by.hackathon.drinder.ui.registration

import androidx.lifecycle.*
import by.hackathon.drinder.UserManager
import by.hackathon.drinder.data.repository.RegistrationRepository
import by.hackathon.drinder.util.ERR_EMPTY_FIELD
import by.hackathon.drinder.util.ERR_PASS_NOT_EQ
import by.hackathon.drinder.util.ERR_REG
import by.hackathon.drinder.util.NO_ERROR
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * '!!' used because this fields has been validated and can't be null.
 * @author Dmitry for mentor Alena :P
 */

@Suppress("MemberVisibilityCanBePrivate")
class RegistrationViewModel @Inject constructor(
    val userManager: UserManager,
    val repository: RegistrationRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val registerNavigationPermission = MutableLiveData(false)
    private val loginErrorField = MutableLiveData<Int>(NO_ERROR)
    private val passErrorField = MutableLiveData<Int>(NO_ERROR)

    val registerNavigationPermissionState: LiveData<Boolean> get() = registerNavigationPermission
    val loginErrorFieldState: LiveData<Int> get() = loginErrorField
    val passErrorFieldState: LiveData<Int> get() = passErrorField

    // DI
//    private val userManager by lazy { getApp().userManager }
//    private val repository: RegistrationRepository by lazy { getApp().repository }

    override fun onStart(owner: LifecycleOwner) {
        registerNavigationPermission.value = false
    }

    fun notifyRegistrationRequest(login: String?, pass: String?, confPass: String?) {
        if (!isValidFields(login, pass, confPass)) return

        viewModelScope.launch {
            val loginInfo = repository.register(login!!, pass!!)
            if (loginInfo != null) {
                userManager.loginInfo = loginInfo
                registerNavigationPermission.value = true
            } else {
                loginErrorField.value = ERR_REG
            }
        }
    }

    private fun isValidFields(login: String?, pass: String?, confPass: String?): Boolean {
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

        if (pass != confPass) {
            passErrorField.value = ERR_PASS_NOT_EQ
            isValid = false
        }
        return isValid
    }
}