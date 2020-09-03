package by.hackathon.drinder.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import by.hackathon.drinder.R
import kotlinx.android.synthetic.main.fragment_user_detail_show.*

class UserDetailFragment : Fragment(R.layout.fragment_user_detail_show) {

    private val viewModel: UserDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.edit_profile) {
            findNavController().navigate(R.id.action_userDetailFragment_to_userDetailEditFragment)
            false
        } else super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModelObservers()
        lifecycle.addObserver(viewModel)
    }

    private fun setupViewModelObservers() {
        viewModel.apply {
            nameState.observe(viewLifecycleOwner) {
                tv_name.text = it
            }
            ageState.observe(viewLifecycleOwner) {
                tv_age.text = it.toString()
            }
            genderState.observe(viewLifecycleOwner) {
                tv_gender.text = it
            }
            alcoholState.observe(viewLifecycleOwner) {
                tv_alcohol.text = it
            }
            connectionErrorState.observe(viewLifecycleOwner) { isError ->
                if (isError)
                    Toast.makeText(
                        context,
                        R.string.error_unable_receive_profile,
                        Toast.LENGTH_LONG
                    ).show()
            }
        }
    }
}