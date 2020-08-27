package by.hackathon.drinder.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.hackathon.drinder.R
import by.hackathon.drinder.api.ApiImplementation
import by.hackathon.drinder.util.myApp
import kotlinx.android.synthetic.main.fragment_user_detail_show.*
import kotlinx.coroutines.*

class UserDetailFragment : Fragment(R.layout.fragment_user_detail_show) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

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
        val userInfo = myApp().userManager.userInfo
        if (userInfo?.alcohol == null) {
            coroutineScope.launch {
                val userInfoD =
                    ApiImplementation.getUserDetail(myApp().userManager.loginInfo?.id ?: "0")
                myApp().userManager.userInfo = userInfoD

                withContext(Dispatchers.Main) {
                    userInfoD?.run {
                        tv_name.text = username
                        tv_age.text = age.toString()
                        tv_gender.text = gender
                        tv_alcohol.text = alcohol
                    }
                }
            }
        } else {
            userInfo.run {
                tv_name.text = username
                tv_age.text = age.toString()
                tv_gender.text = gender
                tv_alcohol.text = alcohol
            }
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.actionBar?.title = getString(R.string.title_user_details_show)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}