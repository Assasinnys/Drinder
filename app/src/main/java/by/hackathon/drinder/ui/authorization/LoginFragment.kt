package by.hackathon.drinder.ui.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.hackathon.drinder.R
import by.hackathon.drinder.api.ApiImplementation
import by.hackathon.drinder.util.mainActivity
import by.hackathon.drinder.util.myApp
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.*

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val navController by lazy { findNavController() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_registration.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registrationFragment)
        }
        btn_login.setOnClickListener {

            val login = tl_login.editText?.text?.toString()
            val pass = tl_password.editText?.text?.toString()

            if (!isValidFields(login, pass)) return@setOnClickListener

            /**
             * '!!' used because this fields has been validated and can't be null.
             * @author Dmitry for mentor Alena :P
             */

            coroutineScope.launch {
                myApp().userManager.loginInfo = ApiImplementation.login(login!!, pass!!)
                withContext(Dispatchers.Main) {
                    if (myApp().userManager.loginInfo?.id != null)
                        navController.navigate(R.id.action_loginFragment_to_mapFragment)
                    else
                        tl_login.error = "User does not exist"
                }
            }
        }
    }

    private fun isValidFields(login: String?, pass: String?): Boolean {
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

    override fun onResume() {
        super.onResume()
        mainActivity().setActionBarTitle(R.string.title_login_screen)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}