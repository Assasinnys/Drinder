package by.hackathon.drinder.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import by.hackathon.drinder.MapsActivity
import by.hackathon.drinder.R
import by.hackathon.drinder.api.ApiImplementation
import by.hackathon.drinder.util.mainActivity
import by.hackathon.drinder.util.myApp
import by.hackathon.drinder.util.myProfileActivity
import kotlinx.android.synthetic.main.fragment_user_detail_show.*
import kotlinx.coroutines.*

class UserDetailFragment : Fragment(R.layout.fragment_user_detail_show) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val userInfo = myApp().userManager.userInfo
        if (userInfo?.alcohol == null) {
            coroutineScope.launch {
                val userInfoD = ApiImplementation.getUserDetail(myApp().userManager.loginInfo?.id?:"0")
                withContext(Dispatchers.Main) {
                    p1.text = getString(R.string.p1, userInfoD?.gender)
                    p2.text = getString(R.string.p2, userInfoD?.age?.toString())
                    p3.text = getString(R.string.p3, userInfoD?.username)
                    p4.text = getString(R.string.p4, userInfoD?.alcohol)
                }
            }
        } else {
            p1.text = getString(R.string.p1, userInfo.gender)
            p2.text = getString(R.string.p2, userInfo.age.toString())
            p3.text = getString(R.string.p3, userInfo.username)
            p4.text = getString(R.string.p4, userInfo.alcohol)
        }

        btn_continue.setOnClickListener {
            startActivity(Intent(context, MapsActivity::class.java))
        }

        im1.setOnClickListener { myProfileActivity().goToUserDetailSettings() }
        im2.setOnClickListener { myProfileActivity().goToUserDetailSettings() }
        im3.setOnClickListener { myProfileActivity().goToUserDetailSettings() }
        im4.setOnClickListener { myProfileActivity().goToUserDetailSettings() }
    }

    override fun onResume() {
        super.onResume()
        myProfileActivity().setActionBarTitle(R.string.title_user_details_show)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}