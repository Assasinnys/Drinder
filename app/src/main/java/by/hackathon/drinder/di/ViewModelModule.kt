package by.hackathon.drinder.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.hackathon.drinder.ui.authorization.LoginViewModel
import by.hackathon.drinder.ui.detail.UserDetailEditViewModel
import by.hackathon.drinder.ui.detail.UserDetailViewModel
import by.hackathon.drinder.ui.map.MapViewModel
import by.hackathon.drinder.ui.registration.RegistrationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun loginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel::class)
    fun registrationViewModel(registrationViewModel: RegistrationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapViewModel::class)
    fun mapViewModel(mapViewModel: MapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserDetailViewModel::class)
    fun userDetailViewModel(UserDetailViewModel: UserDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserDetailEditViewModel::class)
    fun userDetailEditViewModel(UserDetailEditViewModel: UserDetailEditViewModel): ViewModel

    @Singleton
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}