package by.hackathon.drinder.ui.detail

import androidx.lifecycle.*
import by.hackathon.drinder.UserManager
import by.hackathon.drinder.data.UserInfo
import by.hackathon.drinder.data.repository.UserDetailRepository
import by.hackathon.drinder.util.ERR_AGE_TOO_HIGH
import by.hackathon.drinder.util.ERR_AGE_ZERO
import by.hackathon.drinder.util.ERR_EMPTY_FIELD
import by.hackathon.drinder.util.NO_ERROR
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate")
class UserDetailEditViewModel @Inject constructor(
    val userManager: UserManager,
    val repository: UserDetailRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val nameData = MutableLiveData<String>()
    private val nameError = MutableLiveData<Int>(NO_ERROR)
    private val ageData = MutableLiveData<Int>()
    private val ageError = MutableLiveData<Int>(NO_ERROR)
    private val genderData = MutableLiveData<String>()
    private val alcoholData = MutableLiveData<String>()
    private val alcoholError = MutableLiveData<Int>(NO_ERROR)
    private val connectionError = MutableLiveData(false)
    private val saveNavigationPermission = MutableLiveData(false)

    val nameState: LiveData<String> get() = nameData
    val nameErrorState: LiveData<Int> get() = nameError
    val ageState: LiveData<Int> get() = ageData
    val ageErrorState: LiveData<Int> get() = ageError
    val genderState: LiveData<String> get() = genderData
    val alcoholState: LiveData<String> get() = alcoholData
    val alcoholErrorState: LiveData<Int> get() = alcoholError
    val connectionErrorState: LiveData<Boolean> get() = connectionError
    val saveNavigationPermissionState: LiveData<Boolean> get() = saveNavigationPermission

    override fun onStart(owner: LifecycleOwner) {
        connectionError.value = false
        saveNavigationPermission.value = false
        updateUI()
    }

    fun notifyEditInfoRequest(gender: String?, age: String?, alcohol: String?, userName: String?) {
        if (!isValidFields(age, alcohol, userName)) return

        viewModelScope.launch {
            userManager.loginInfo?.let {
                val isSuccess = repository.postUserDetail(
                    it.login,
                    it.pass,
                    gender ?: "",
                    age!!.toInt(),
                    alcohol!!,
                    userName!!
                )
                if (isSuccess) {
                    userManager.userInfo = UserInfo(alcohol, gender, age.toInt(), userName)
                    saveNavigationPermission.value = true
                } else
                    connectionError.value = true
            }
        }
    }

    private fun updateUI() {
        userManager.userInfo?.let { info ->
            nameData.value = info.username
            ageData.value = info.age
            genderData.value = info.gender
            alcoholData.value = info.alcohol
        }
    }

    private fun isValidFields(age: String?, alcohol: String?, name: String?): Boolean {
        var isValid = true

        when {
            age.isNullOrEmpty() -> {
                ageError.value = ERR_EMPTY_FIELD
                isValid = false
            }
            age.toInt() <= 0 -> {
                ageError.value = ERR_AGE_ZERO
                isValid = false
            }
            age.toInt() > 150 -> {
                ageError.value = ERR_AGE_TOO_HIGH
                isValid = false
            }
            else -> {
                ageError.value = NO_ERROR
            }
        }

        if (alcohol.isNullOrEmpty()) {
            alcoholError.value = ERR_EMPTY_FIELD
            isValid = false
        } else {
            alcoholError.value = NO_ERROR
        }

        if (name.isNullOrEmpty()) {
            nameError.value = ERR_EMPTY_FIELD
            isValid = false
        } else {
            nameError.value = NO_ERROR
        }
        return isValid
    }
}