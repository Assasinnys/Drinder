package by.hackathon.drinder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import by.hackathon.drinder.R
import by.hackathon.drinder.util.isLocationPermissionGranted
import by.hackathon.drinder.util.requestLocationPermission

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isLocationPermissionGranted()) requestLocationPermission()

        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment))

        /*supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.animator.slide_right, R.animator.slide_left)
            replace(R.id.f_container, LoginFragment())
            commit()
        }*/
    }

    /*fun goToDetailShowFragment() {
        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.animator.slide_right, R.animator.slide_left, R.animator.slide_back_left, R.animator.slide_back_right)
            replace(R.id.f_container, UserDetailFragment())
            addToBackStack("detail")
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
    }*/

    fun setActionBarTitle(resId: Int) {
        supportActionBar?.title = getString(resId)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }
}