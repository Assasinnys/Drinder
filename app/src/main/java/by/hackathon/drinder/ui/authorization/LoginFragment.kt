package by.hackathon.drinder.ui.authorization

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import by.hackathon.drinder.MapsActivity
import by.hackathon.drinder.R
import by.hackathon.drinder.util.mainActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_registration.setOnClickListener {
            mainActivity().goToRegistration()
        }
        btn_login.setOnClickListener {
            //TODO request
//            startActivity(Intent(context, MapsActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity().setActionBarTitle(R.string.title_login_screen)
    }
}