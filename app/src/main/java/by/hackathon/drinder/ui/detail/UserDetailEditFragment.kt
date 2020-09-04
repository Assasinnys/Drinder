package by.hackathon.drinder.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import by.hackathon.drinder.R
import by.hackathon.drinder.util.setEditTextError
import kotlinx.android.synthetic.main.fragment_user_detail_edit.*

class UserDetailEditFragment : Fragment(R.layout.fragment_user_detail_edit) {

    private val navController by lazy { findNavController() }
    private val viewModel: UserDetailEditViewModel by viewModels()

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
            nameState.observe(viewLifecycleOwner) {
                tl_name.editText?.setText(it)
            }
            ageState.observe(viewLifecycleOwner) {
                tl_age.editText?.setText(it.toString())
            }
            genderState.observe(viewLifecycleOwner) {
                tl_gender.editText?.setText(it)
            }
            alcoholState.observe(viewLifecycleOwner) {
                tl_alcohol.editText?.setText(it)
            }
            nameErrorState.observe(viewLifecycleOwner) { code ->
                setEditTextError(tl_name, code)
            }
            ageErrorState.observe(viewLifecycleOwner) { code ->
                setEditTextError(tl_age, code)
            }
            alcoholErrorState.observe(viewLifecycleOwner) { code ->
                setEditTextError(tl_alcohol, code)
            }
            connectionErrorState.observe(viewLifecycleOwner) { isError ->
                if (isError)
                    Toast.makeText(
                        context,
                        R.string.error_save_data,
                        Toast.LENGTH_SHORT
                    ).show()
            }
            saveNavigationPermissionState.observe(viewLifecycleOwner) { isGranted ->
                if (isGranted) navController.navigate(R.id.action_userDetailEditFragment_to_mapFragment)
            }
        }
    }
}