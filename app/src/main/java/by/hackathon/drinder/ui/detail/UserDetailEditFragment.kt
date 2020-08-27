package by.hackathon.drinder.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.hackathon.drinder.R
import by.hackathon.drinder.api.ApiImplementation
import by.hackathon.drinder.util.myApp
import kotlinx.android.synthetic.main.fragment_user_detail_edit.*
import kotlinx.coroutines.*

class UserDetailEditFragment : Fragment(R.layout.fragment_user_detail_edit) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val navController by lazy { findNavController() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initFields()

        btn_save.setOnClickListener {
            val age = tl_age.editText?.text?.toString()
            val gender = tl_gender.editText?.text?.toString()
            val alcohol = tl_alcohol.editText?.text?.toString()
            val name = tl_name.editText?.text?.toString()
            val login = myApp().userManager.loginInfo?.login!!
            val pass = myApp().userManager.loginInfo?.pass!!

            if (isValidFields(age, alcohol, name)) {
                coroutineScope.launch {
                    if (ApiImplementation.postUserDetail(login, pass, gender ?: "", age!!.toInt(), alcohol!!, name!!)) {
                        withContext(Dispatchers.Main) {
                            navController.navigate(R.id.action_userDetailEditFragment_to_mapFragment)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, R.string.error_save_data, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun initFields() {
        val userInfo = myApp().userManager.userInfo

        userInfo?.let { info ->
            tl_name.editText?.setText(info.username)
            tl_alcohol.editText?.setText(info.alcohol)
            tl_age.editText?.setText(info.age.toString())
            tl_gender.editText?.setText(info.gender)
        }
    }

    private fun isValidFields(age: String?, alcohol: String?, name: String?): Boolean {
        when {
            age.isNullOrEmpty() -> {
                tl_age.apply {
                    isErrorEnabled = true
                    error = getString(R.string.error_empty_field)
                }
                return false
            }
            age.toInt() <= 0 -> {
                tl_age.apply {
                    isErrorEnabled = true
                    error = getString(R.string.error_age_positive)
                }
                return false
            }
            age.toInt() > 150 -> {
                tl_age.apply {
                    isErrorEnabled = true
                    error = getString(R.string.error_age_too_big)
                }
                return false
            }
            alcohol.isNullOrEmpty() -> {
                tl_alcohol.apply {
                    isErrorEnabled = true
                    error = getString(R.string.error_empty_field)
                }
                return false
            }
            name.isNullOrEmpty() -> {
                tl_name.apply {
                    isErrorEnabled = true
                    error = getString(R.string.error_empty_field)
                }
                return false
            }
            else -> {
                tl_age.isErrorEnabled = false
                tl_alcohol.isErrorEnabled = false
                tl_name.isErrorEnabled = false
                return true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.actionBar?.title = getString(R.string.title_user_details_edit)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}