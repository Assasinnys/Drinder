package by.hackathon.drinder.ui.detail

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.hackathon.drinder.MapsActivity
import by.hackathon.drinder.R
import by.hackathon.drinder.api.ApiImplementation
import by.hackathon.drinder.util.mainActivity
import by.hackathon.drinder.util.myApp
import by.hackathon.drinder.util.myProfileActivity
import kotlinx.android.synthetic.main.fragment_user_detail_edit.*
import kotlinx.coroutines.*

class UserDetailEditFragment : Fragment(R.layout.fragment_user_detail_edit) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val navController by lazy { findNavController() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_continue.setOnClickListener {
            val age = tl_age.editText?.text?.toString()!!
            val gender = tl_gender.editText?.text?.toString()!!
            val alcohol = tl_alcohol.editText?.text?.toString()!!
            val name = tl_name.editText?.text?.toString()!!
            val login = myApp().userManager.loginInfo?.login!!
            val pass = myApp().userManager.loginInfo?.pass!!

            coroutineScope.launch {
                if (ApiImplementation.postUserDetail(login, pass, gender, age.toInt(), alcohol, name)) {
                    withContext(Dispatchers.Main) {
                        navController.navigate(R.id.action_userDetailEditFragment_to_userDetailFragment)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error saving data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.actionBar?.title = getString(R.string.title_user_details_edit)
        //mainActivity().setActionBarTitle(R.string.title_user_details_edit)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}