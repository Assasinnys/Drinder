package by.hackathon.drinder.ui.detail

import androidx.lifecycle.*
import by.hackathon.drinder.UserManager
import by.hackathon.drinder.data.UserInfo
import by.hackathon.drinder.data.repository.UserDetailRepository
import by.hackathon.drinder.util.DEFAULT_USER
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate")
class UserDetailViewModel @Inject constructor(
    val userManager: UserManager,
    val repository: UserDetailRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val nameData = MutableLiveData<String>()
    private val ageData = MutableLiveData<Int>()
    private val genderData = MutableLiveData<String>()
    private val alcoholData = MutableLiveData<String>()
    private val connectionError = MutableLiveData(false)
    private val _isUserDifferent = MutableLiveData(false)

    val nameState: LiveData<String> get() = nameData
    val ageState: LiveData<Int> get() = ageData
    val genderState: LiveData<String> get() = genderData
    val alcoholState: LiveData<String> get() = alcoholData
    val connectionErrorState: LiveData<Boolean> get() = connectionError
    val isUserDifferent: LiveData<Boolean> get() = _isUserDifferent

    private var userId: String = DEFAULT_USER

    override fun onStart(owner: LifecycleOwner) {
        connectionError.value = false
        if (userId == DEFAULT_USER) {
            val userInfo = userManager.userInfo
            if (userInfo != null) {
                updateUI(userInfo)
            } else {
                userManager.loginInfo?.let {
                    requestUserInfo(it.id, true)
                }
            }
            _isUserDifferent.value = false
        } else {
            requestUserInfo(userId, false)
            _isUserDifferent.value = true
        }
    }

    private fun requestUserInfo(id: String, isSaveRequired: Boolean) {
        viewModelScope.launch {
            val newUserInfo = repository.getUserDetail(id)
            if (newUserInfo != null) {
                updateUI(newUserInfo)
                if (isSaveRequired) userManager.userInfo = newUserInfo
            } else {
                connectionError.value = true
            }
        }
    }

    private fun updateUI(userInfo: UserInfo) {
        userInfo.apply {
            nameData.value = username
            ageData.value = age
            genderData.value = gender
            alcoholData.value = alcohol
        }
    }

    fun notifyDifferentUserIdSent(id: String) {
        userId = id
    }
}