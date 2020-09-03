package by.hackathon.drinder

import android.app.Application
import by.hackathon.drinder.data.Repository

class App : Application() {
    lateinit var userManager: UserManager
    lateinit var repository: Repository

    override fun onCreate() {
        super.onCreate()
        repository = Repository()
        userManager = UserManager()
    }
}