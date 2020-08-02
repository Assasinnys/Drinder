package by.hackathon.drinder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.hackathon.drinder.R
import by.hackathon.drinder.ui.authorization.LoginFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.animator.slide_right, R.animator.slide_left/*, R.animator.slide_back_right, R.animator.slide_back_left*/)
            replace(R.id.f_container, LoginFragment())
//            addToBackStack("login")
            commit()
        }
    }
}