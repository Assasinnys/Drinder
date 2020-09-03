package by.hackathon.drinder.ui.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import by.hackathon.drinder.R
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val navController by lazy { findNavController() }

    private val viewModel: LoginViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModelObservers()
        lifecycle.addObserver(viewModel)

        btn_registration.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registrationFragment)
        }

        btn_login.setOnClickListener {
            val login = tl_login.editText?.text?.toString()
            val pass = tl_password.editText?.text?.toString()
            viewModel.notifyLoginRequest(login, pass)
        }
    }

    private fun setupViewModelObservers() {
        viewModel.apply {
            loginErrorFieldState.observe(viewLifecycleOwner) {
                tl_login.apply {
                    isErrorEnabled = it.second
                    error = it.first
                }
            }
            passErrorFieldState.observe(viewLifecycleOwner) {
                tl_password.apply {
                    isErrorEnabled = it.second
                    error = it.first
                }
            }
            loginNavigationPermissionState.observe(viewLifecycleOwner) { isGranted ->
                if (isGranted) navController.navigate(R.id.action_loginFragment_to_mapFragment)
            }
        }
    }
}