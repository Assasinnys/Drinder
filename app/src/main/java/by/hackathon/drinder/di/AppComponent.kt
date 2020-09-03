package by.hackathon.drinder.di

import android.content.Context
import dagger.Component

@Component
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(context: Context): AppComponent
    }


}