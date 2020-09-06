package by.hackathon.drinder

import android.app.Application
import by.hackathon.drinder.di.DaggerAppComponent

class App : Application() {
    val appComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}
