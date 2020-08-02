package by.hackathon.drinder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.hackathon.drinder.R
import by.hackathon.drinder.ui.authorization.LoginFragment
import by.hackathon.drinder.ui.authorization.RegistrationFragment
import by.hackathon.drinder.ui.detail.UserDetailEditFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.animator.slide_right, R.animator.slide_left)
            replace(R.id.f_container, LoginFragment())
            commit()
        }
    }

    fun goToRegistration() {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.animator.slide_right, R.animator.slide_left, R.animator.slide_back_left, R.animator.slide_back_right)
            replace(R.id.f_container, RegistrationFragment())
            addToBackStack("login")
            commit()
        }
    }

    fun goToUserDetailSettings() {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.animator.slide_right, R.animator.slide_left, R.animator.slide_back_left, R.animator.slide_back_right)
            replace(R.id.f_container, UserDetailEditFragment())
            addToBackStack("edit")
            commit()
        }
    }

    fun setActionBarTitle(resId: Int) {
        supportActionBar?.title = getString(resId)
    }
}