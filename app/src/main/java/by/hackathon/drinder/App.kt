package by.hackathon.drinder

import android.app.Application

class App : Application() {
    val userManager: UserManager = UserManager()
}