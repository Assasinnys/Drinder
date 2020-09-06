package by.hackathon.drinder.ui.detail

import androidx.lifecycle.*
import by.hackathon.drinder.data.UserInfo
import by.hackathon.drinder.data.repository.UserDetailRepository
import by.hackathon.drinder.util.DEFAULT_USER
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate")
class UserDetailViewModel @Inject constructor(
//    val userManager: UserManager,
    val repository: UserDetailRepository
) : ViewModel(), DefaultLifecycleObserver {

    private val _nameData = MutableLiveData<String>()
    private val _ageData = MutableLiveData<Int>()
    private val _genderData = MutableLiveData<String>()
    private val _alcoholData = MutableLiveData<String>()
    private val _connectionError = MutableLiveData(false)
    private val _isEditDisabled = MutableLiveData(false)

    val nameData: LiveData<String> get() = _nameData
    val ageData: LiveData<Int> get() = _ageData
    val genderData: LiveData<String> get() = _genderData
    val alcoholData: LiveData<String> get() = _alcoholData
    val connectionError: LiveData<Boolean> get() = _connectionError
    val isEditDisabled: LiveData<Boolean> get() = _isEditDisabled

    private var userId: String = DEFAULT_USER

    override fun onStart(owner: LifecycleOwner) {
        _connectionError.value = false
        if (userId == DEFAULT_USER) {
            val userInfo = repository.getSavedUserDetails()
            if (userInfo != null) {
                updateUI(userInfo)
            } else {
                requestUserInfo(DEFAULT_USER)
            }
            _isEditDisabled.value = false
        } else {
            requestUserInfo(userId)
            _isEditDisabled.value = true
        }
    }

    private fun requestUserInfo(id: String) {
        viewModelScope.launch {
            val newUserDetail = if (id == DEFAULT_USER)
                repository.getOwnUserDetail()
            else
                repository.getUserDetail(id)

            if (newUserDetail != null)
                updateUI(newUserDetail)
            else
                _connectionError.value = true
        }
    }

    private fun updateUI(userInfo: UserInfo) {
        userInfo.apply {
            _nameData.value = username
            _ageData.value = age
            _genderData.value = gender
            _alcoholData.value = alcohol
        }
    }

    fun notifyDifferentUserIdSent(id: String) {
        userId = id
    }
}
