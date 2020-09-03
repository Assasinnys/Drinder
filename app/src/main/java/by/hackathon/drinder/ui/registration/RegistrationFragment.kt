package by.hackathon.drinder.ui.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import by.hackathon.drinder.R
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private val navController by lazy { findNavController() }
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModelObservers()
        lifecycle.addObserver(viewModel)

        btn_register.setOnClickListener {
            val login = tl_login.editText?.text?.toString()
            val pass = tl_password.editText?.text?.toString()
            val confPass = tl_confirm_password?.editText?.text?.toString()
            viewModel.notifyRegistrationRequest(login, pass, confPass)
        }
    }

    private fun setupViewModelObservers() {
        viewModel.apply {
            registerNavigationPermissionState.observe(viewLifecycleOwner) {isGranted ->
                if (isGranted) navController.navigate(R.id.action_registrationFragment_to_userDetailEditFragment)
            }
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
        }
    }
}