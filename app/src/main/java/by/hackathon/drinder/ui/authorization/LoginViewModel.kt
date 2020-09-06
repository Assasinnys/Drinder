package by.hackathon.drinder.ui.authorization

import androidx.lifecycle.*
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
    val repository: LoginRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val _loginNavigationPermission = MutableLiveData(false)
    private val _loginErrorField = MutableLiveData<Int>(NO_ERROR)
    private val _passErrorField = MutableLiveData<Int>(NO_ERROR)
    private val _login = MutableLiveData<String>()
    private val _pass = MutableLiveData<String>()

    val loginNavigationPermission: LiveData<Boolean> get() = _loginNavigationPermission
    val loginErrorField: LiveData<Int> get() = _loginErrorField
    val passErrorField: LiveData<Int> get() = _passErrorField
    val login: LiveData<String> get() = _login
    val pass: LiveData<String> get() = _pass

    override fun onStart(owner: LifecycleOwner) {
        val previousLoginData = repository.getPreviousLoginData()
        _login.value = previousLoginData.first
        _pass.value = previousLoginData.second
        repository.logoutUser()
        _loginNavigationPermission.value = false
    }

    fun notifyLoginRequest(login: String?, pass: String?) {
        if (!isValidFields(login, pass)) return

        viewModelScope.launch {
            val isSuccessful = repository.login(login!!, pass!!)
            if (isSuccessful) {
                _loginNavigationPermission.value = true
            } else {
                _loginErrorField.value = ERR_USER_NOT_EXIST
            }
        }
    }

    private fun isValidFields(login: String?, pass: String?): Boolean {
        var isValid = true

        if (login.isNullOrEmpty()) {
            _loginErrorField.value = ERR_EMPTY_FIELD
            isValid = false
        } else {
            _loginErrorField.value = NO_ERROR
        }

        if (pass.isNullOrEmpty()) {
            _passErrorField.value = ERR_EMPTY_FIELD
            isValid = false
        } else {
            _passErrorField.value = NO_ERROR
        }

        return isValid
    }
}
