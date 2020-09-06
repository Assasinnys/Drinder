package by.hackathon.drinder.ui.detail

import androidx.lifecycle.*
import by.hackathon.drinder.data.repository.UserDetailRepository
import by.hackathon.drinder.util.ERR_AGE_TOO_HIGH
import by.hackathon.drinder.util.ERR_AGE_ZERO
import by.hackathon.drinder.util.ERR_EMPTY_FIELD
import by.hackathon.drinder.util.NO_ERROR
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate")
class UserDetailEditViewModel @Inject constructor(
    val repository: UserDetailRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val _nameData = MutableLiveData<String>()
    private val _nameError = MutableLiveData<Int>(NO_ERROR)
    private val _ageData = MutableLiveData<Int>()
    private val _ageError = MutableLiveData<Int>(NO_ERROR)
    private val _genderData = MutableLiveData<String>()
    private val _alcoholData = MutableLiveData<String>()
    private val _alcoholError = MutableLiveData<Int>(NO_ERROR)
    private val _connectionError = MutableLiveData(false)
    private val _saveNavigationPermission = MutableLiveData(false)

    val nameData: LiveData<String> get() = _nameData
    val nameError: LiveData<Int> get() = _nameError
    val ageData: LiveData<Int> get() = _ageData
    val ageError: LiveData<Int> get() = _ageError
    val genderData: LiveData<String> get() = _genderData
    val alcoholData: LiveData<String> get() = _alcoholData
    val alcoholError: LiveData<Int> get() = _alcoholError
    val connectionError: LiveData<Boolean> get() = _connectionError
    val saveNavigationPermission: LiveData<Boolean> get() = _saveNavigationPermission

    override fun onStart(owner: LifecycleOwner) {
        _connectionError.value = false
        _saveNavigationPermission.value = false
        updateUI()
    }

    fun notifyEditInfoRequest(gender: String?, age: String?, alcohol: String?, userName: String?) {
        if (!isValidFields(age, alcohol, userName)) return

        viewModelScope.launch {
            val isSuccess = repository.postUserDetail(
                gender ?: "",
                age!!.toInt(),
                alcohol!!,
                userName!!
            )
            if (isSuccess) {
                _saveNavigationPermission.value = true
            } else
                _connectionError.value = true
        }
    }


    private fun updateUI() {
        repository.getSavedUserDetails()?.let { info ->
            _nameData.value = info.username
            _ageData.value = info.age
            _genderData.value = info.gender
            _alcoholData.value = info.alcohol
        }
    }

    private fun isValidFields(age: String?, alcohol: String?, name: String?): Boolean {
        var isValid = true

        when {
            age.isNullOrEmpty() -> {
                _ageError.value = ERR_EMPTY_FIELD
                isValid = false
            }
            age.toInt() <= 0 -> {
                _ageError.value = ERR_AGE_ZERO
                isValid = false
            }
            age.toInt() > 150 -> {
                _ageError.value = ERR_AGE_TOO_HIGH
                isValid = false
            }
            else -> {
                _ageError.value = NO_ERROR
            }
        }

        if (alcohol.isNullOrEmpty()) {
            _alcoholError.value = ERR_EMPTY_FIELD
            isValid = false
        } else {
            _alcoholError.value = NO_ERROR
        }

        if (name.isNullOrEmpty()) {
            _nameError.value = ERR_EMPTY_FIELD
            isValid = false
        } else {
            _nameError.value = NO_ERROR
        }
        return isValid
    }
}
