package by.hackathon.drinder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.hackathon.drinder.R
import by.hackathon.drinder.ui.detail.UserDetailFragment

class MyProfileActivity: AppCompatActivity(R.layout.activity_my_profile) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(R.animator.slide_right, R.animator.slide_left)
            replace(R.id.fa_container, UserDetailFragment())
            commit()
        }
    }

    fun setActionBarTitle(resId: Int) {
        supportActionBar?.title = getString(resId)
    }
}