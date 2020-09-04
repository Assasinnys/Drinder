package by.hackathon.drinder

import android.app.Application
import by.hackathon.drinder.di.DaggerAppComponent

class App : Application() {
//    lateinit var userManager: UserManager
//    lateinit var repository: Repository
    val appComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()
//        repository = Repository()
//        userManager = UserManager()
    }
}