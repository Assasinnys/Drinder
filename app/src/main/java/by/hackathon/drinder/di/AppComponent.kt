package by.hackathon.drinder.di

import android.content.Context
import by.hackathon.drinder.ui.authorization.LoginFragment
import by.hackathon.drinder.ui.detail.UserDetailEditFragment
import by.hackathon.drinder.ui.detail.UserDetailFragment
import by.hackathon.drinder.ui.map.MapFragment
import by.hackathon.drinder.ui.registration.RegistrationFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [ViewModelModule::class, RepositoryModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(loginFragment: LoginFragment)
    fun inject(registrationFragment: RegistrationFragment)
    fun inject(mapFragment: MapFragment)
    fun inject(userDetailFragment: UserDetailFragment)
    fun inject(userDetailEditFragment: UserDetailEditFragment)
}