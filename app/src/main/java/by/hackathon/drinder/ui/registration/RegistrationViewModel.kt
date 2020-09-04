package by.hackathon.drinder.ui.registration

import android.app.Application
import androidx.lifecycle.*
import by.hackathon.drinder.data.repository.RegisterRepository
import by.hackathon.drinder.util.*
import kotlinx.coroutines.launch

/**
 * '!!' used because this fields has been validated and can't be null.
 * @author Dmitry for mentor Alena :P
 */

class RegistrationViewModel(app: Application) : AndroidViewModel(app), DefaultLifecycleObserver {

    private val registerNavigationPermission = MutableLiveData(false)
    private val loginErrorField = MutableLiveData<Int>(NO_ERROR)
    private val passErrorField = MutableLiveData<Int>(NO_ERROR)

    val registerNavigationPermissionState: LiveData<Boolean> get() = registerNavigationPermission
    val loginErrorFieldState: LiveData<Int> get() = loginErrorField
    val passErrorFieldState: LiveData<Int> get() = passErrorField

    // DI
    private val userManager by lazy { getApp().userManager }
    private val repository: RegisterRepository by lazy { getApp().repository }

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