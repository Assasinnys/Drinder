package by.hackathon.drinder.ui.detail

import androidx.lifecycle.*
import by.hackathon.drinder.UserManager
import by.hackathon.drinder.data.UserInfo
import by.hackathon.drinder.data.repository.UserDetailRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate")
class UserDetailViewModel @Inject constructor(val userManager: UserManager, val repository: UserDetailRepository) : ViewModel(), DefaultLifecycleObserver {

    private val nameData = MutableLiveData<String>()
    private val ageData = MutableLiveData<Int>()
    private val genderData = MutableLiveData<String>()
    private val alcoholData = MutableLiveData<String>()
    private val connectionError = MutableLiveData(false)

    val nameState: LiveData<String> get() = nameData
    val ageState: LiveData<Int> get() = ageData
    val genderState: LiveData<String> get() = genderData
    val alcoholState: LiveData<String> get() = alcoholData
    val connectionErrorState: LiveData<Boolean> = connectionError

    // DI
//    private val userManager by lazy { getApp().userManager }
//    private val repository: UserDetailRepository by lazy { getApp().repository }

    override fun onStart(owner: LifecycleOwner) {
        connectionError.value = false
        val userInfo = userManager.userInfo
        if (userInfo != null) {
            updateUI(userInfo)
        } else {
            viewModelScope.launch {
                userManager.loginInfo?.let {
                    val newUserInfo = repository.getUserDetail(it.id)
                    if (newUserInfo != null) {
                        userManager.userInfo = newUserInfo
                        updateUI(newUserInfo)
                    } else {
                        connectionError.value = true
                    }
                }
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
}