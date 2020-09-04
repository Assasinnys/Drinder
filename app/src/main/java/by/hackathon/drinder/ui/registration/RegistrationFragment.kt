package by.hackathon.drinder.ui.registration

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
import kotlinx.android.synthetic.main.fragment_registration.*
import javax.inject.Inject

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private val navController by lazy { findNavController() }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: RegistrationViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        daggerAppComponent().inject(this)
    }

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
            registerNavigationPermissionState.observe(viewLifecycleOwner) { isGranted ->
                if (isGranted) navController.navigate(R.id.action_registrationFragment_to_userDetailEditFragment)
            }
            loginErrorFieldState.observe(viewLifecycleOwner) { code ->
                setEditTextError(tl_login, code)
            }
            passErrorFieldState.observe(viewLifecycleOwner) { code ->
                setEditTextError(tl_password, code)
            }
        }
    }
}