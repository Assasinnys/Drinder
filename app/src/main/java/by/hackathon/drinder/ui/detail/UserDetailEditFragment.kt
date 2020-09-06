package by.hackathon.drinder.ui.detail

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import by.hackathon.drinder.R
import by.hackathon.drinder.di.ViewModelFactory
import by.hackathon.drinder.util.daggerAppComponent
import by.hackathon.drinder.util.setEditTextError
import kotlinx.android.synthetic.main.fragment_user_detail_edit.*
import javax.inject.Inject

class UserDetailEditFragment : Fragment(R.layout.fragment_user_detail_edit) {

    private val navController by lazy { findNavController() }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: UserDetailEditViewModel by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        daggerAppComponent().inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModelObservers()
        lifecycle.addObserver(viewModel)

        btn_save.setOnClickListener {
            val age = tl_age.editText?.text?.toString()
            val gender = tl_gender.editText?.text?.toString()
            val alcohol = tl_alcohol.editText?.text?.toString()
            val name = tl_name.editText?.text?.toString()

            viewModel.notifyEditInfoRequest(gender, age, alcohol, name)
        }
    }

    private fun setupViewModelObservers() {
        viewModel.apply {
            nameData.observe(viewLifecycleOwner) {
                tl_name.editText?.setText(it)
            }
            ageData.observe(viewLifecycleOwner) {
                tl_age.editText?.setText(it.toString())
            }
            genderData.observe(viewLifecycleOwner) {
                tl_gender.editText?.setText(it)
            }
            alcoholData.observe(viewLifecycleOwner) {
                tl_alcohol.editText?.setText(it)
            }
            nameError.observe(viewLifecycleOwner) { code ->
                setEditTextError(tl_name, code)
            }
            ageError.observe(viewLifecycleOwner) { code ->
                setEditTextError(tl_age, code)
            }
            alcoholError.observe(viewLifecycleOwner) { code ->
                setEditTextError(tl_alcohol, code)
            }
            connectionError.observe(viewLifecycleOwner) { isError ->
                if (isError)
                    Toast.makeText(
                        context,
                        R.string.error_save_data,
                        Toast.LENGTH_SHORT
                    ).show()
            }
            saveNavigationPermission.observe(viewLifecycleOwner) { isGranted ->
                if (isGranted) navController.navigate(R.id.action_userDetailEditFragment_to_mapFragment)
            }
        }
    }
}
