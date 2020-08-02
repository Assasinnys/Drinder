package by.hackathon.drinder.ui.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import by.hackathon.drinder.R
import by.hackathon.drinder.util.mainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_register.setOnClickListener {
            // TODO request
            mainActivity().goToUserDetailSettings()
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivity().setActionBarTitle(R.string.title_registration)
    }
}