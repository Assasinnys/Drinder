package by.hackathon.drinder

import android.app.Application

class App : Application() {
    lateinit var userManager: UserManager

    override fun onCreate() {
        super.onCreate()
        userManager = UserManager(applicationContext)
    }
}