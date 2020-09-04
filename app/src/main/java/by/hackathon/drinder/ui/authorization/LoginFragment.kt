package by.hackathon.drinder.ui.authorization

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import by.hackathon.drinder.R
import by.hackathon.drinder.di.ViewModelFactory
import by.hackathon.drinder.util.daggerAppComponent
import by.hackathon.drinder.util.setEditTextError
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

class LoginFragment : Fragment(R.layout.fragment_login) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: LoginViewModel by viewModels { viewModelFactory }

    private val navController by lazy { findNavController() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        daggerAppComponent().inject(this)
    }

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
            loginErrorFieldState.observe(viewLifecycleOwner) {code ->
                setEditTextError(tl_login, code)
            }
            passErrorFieldState.observe(viewLifecycleOwner) {code ->
                setEditTextError(tl_password, code)
            }
            loginNavigationPermissionState.observe(viewLifecycleOwner) { isGranted ->
                if (isGranted) navController.navigate(R.id.action_loginFragment_to_mapFragment)
            }
        }
    }
}