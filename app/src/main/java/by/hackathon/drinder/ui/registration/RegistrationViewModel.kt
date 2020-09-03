package by.hackathon.drinder.ui.registration

import android.app.Application
import androidx.lifecycle.*
import by.hackathon.drinder.data.repository.RegisterRepository
import by.hackathon.drinder.ui.authorization.LoginViewModel
import by.hackathon.drinder.util.getApp
import kotlinx.coroutines.launch

/**
 * '!!' used because this fields has been validated and can't be null.
 * @author Dmitry for mentor Alena :P
 */

class RegistrationViewModel(app: Application) : AndroidViewModel(app), DefaultLifecycleObserver {

    private val registerNavigationPermission = MutableLiveData(false)
    private val loginErrorField = MutableLiveData<Pair<String, Boolean>>(LoginViewModel.NO_ERROR)
    private val passErrorField = MutableLiveData<Pair<String, Boolean>>(LoginViewModel.NO_ERROR)

    val registerNavigationPermissionState: LiveData<Boolean> get() = registerNavigationPermission
    val loginErrorFieldState: LiveData<Pair<String, Boolean>> get() = loginErrorField
    val passErrorFieldState: LiveData<Pair<String, Boolean>> get() = passErrorField

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
            loginErrorField.value = LoginViewModel.ERR_EMPTY_FIELD
            isValid = false
        } else {
            loginErrorField.value = LoginViewModel.NO_ERROR
        }

        if (pass.isNullOrEmpty()) {
            passErrorField.value = LoginViewModel.ERR_EMPTY_FIELD
            isValid = false
        } else {
            passErrorField.value = LoginViewModel.NO_ERROR
        }

        if (pass != confPass) {
            passErrorField.value = ERR_PASS_NOT_EQ
            isValid = false
        }

        return isValid
    }

    companion object {
        val ERR_PASS_NOT_EQ = "Passwords are not equals" to true
        val ERR_REG = "Registration error" to true
    }
}