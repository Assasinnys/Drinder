package by.hackathon.drinder.ui.registration

import androidx.lifecycle.*
import by.hackathon.drinder.data.repository.RegistrationRepository
import by.hackathon.drinder.util.ERR_EMPTY_FIELD
import by.hackathon.drinder.util.ERR_PASS_NOT_EQ
import by.hackathon.drinder.util.ERR_REG
import by.hackathon.drinder.util.NO_ERROR
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * '!!' used because this fields has been validated and can't be null.
 */

@Suppress("MemberVisibilityCanBePrivate")
class RegistrationViewModel @Inject constructor(
    val repository: RegistrationRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val _registerNavigationPermission = MutableLiveData(false)
    private val _loginErrorField = MutableLiveData<Int>(NO_ERROR)
    private val _passErrorField = MutableLiveData<Int>(NO_ERROR)

    val registerNavigationPermission: LiveData<Boolean> get() = _registerNavigationPermission
    val loginErrorField: LiveData<Int> get() = _loginErrorField
    val passErrorField: LiveData<Int> get() = _passErrorField

    override fun onStart(owner: LifecycleOwner) {
        _registerNavigationPermission.value = false
    }

    fun notifyRegistrationRequest(login: String?, pass: String?, confPass: String?) {
        if (!isValidFields(login, pass, confPass)) return

        viewModelScope.launch {
            val isSuccessful = repository.register(login!!, pass!!)
            if (isSuccessful)
                _registerNavigationPermission.value = true
            else
                _loginErrorField.value = ERR_REG
        }
    }

    private fun isValidFields(login: String?, pass: String?, confPass: String?): Boolean {
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

        if (pass != confPass) {
            _passErrorField.value = ERR_PASS_NOT_EQ
            isValid = false
        }
        return isValid
    }
}
