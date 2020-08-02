package by.hackathon.drinder.ui.authorization

import android.os.Bundle
import androidx.fragment.app.Fragment
import by.hackathon.drinder.R
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_registration.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(R.animator.slide_right, R.animator.slide_left, R.animator.slide_back_left, R.animator.slide_back_right)
                replace(R.id.f_container, RegistrationFragment())
                addToBackStack("login")
                commit()
            }
        }
    }
}