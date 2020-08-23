package by.hackathon.drinder.ui.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.hackathon.drinder.R
import by.hackathon.drinder.api.ApiImplementation
import by.hackathon.drinder.util.mainActivity
import by.hackathon.drinder.util.myApp
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.coroutines.*

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val navController by lazy { findNavController() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_register.setOnClickListener {

            val login = tl_login.editText?.text?.toString()
            val pass = tl_password.editText?.text?.toString()
            val confPass = tl_confirm_password?.editText?.text?.toString()

            /**
             * '!!' used because this fields has been validated and can't be null.
             * @author Dmitry for mentor Alena :P
             */

            if (!isValidFields(login, pass, confPass)) return@setOnClickListener

            coroutineScope.launch {
                myApp().userManager.loginInfo = ApiImplementation.register(login!!, pass!!)
                withContext(Dispatchers.Main) {
                    if (myApp().userManager.loginInfo?.id != null)
                        navController.navigate(R.id.action_registrationFragment_to_userDetailEditFragment)
                    else tl_login.error = "Registration error"
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity().setActionBarTitle(R.string.title_registration)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    private fun isValidFields(login: String?, pass: String?, confPass: String?): Boolean {

        if (login.isNullOrEmpty() || pass.isNullOrEmpty()) {
            if (login.isNullOrEmpty()) {
                tl_login.apply {
                    isErrorEnabled = true
                    error = "Empty field"
                }
            } else {
                tl_password.apply {
                    isErrorEnabled = true
                    error = "Empty field"
                }
            }
            return false
        } else if (pass != confPass) {
            tl_password.apply {
                isErrorEnabled = true
                error = "Passwords not equals"
            }
            return false
        } else {
            tl_login.apply {
                isErrorEnabled = false
                error = ""
            }
            tl_password.apply {
                isErrorEnabled = false
                error = ""
            }
        }
        return true
    }
}