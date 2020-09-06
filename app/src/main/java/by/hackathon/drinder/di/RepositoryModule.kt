package by.hackathon.drinder.di

import by.hackathon.drinder.data.SharedPreferenceStorage
import by.hackathon.drinder.data.Storage
import by.hackathon.drinder.data.repository.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun getLoginRepository(mainRepository: MainRepository): LoginRepository

    @Binds
    @Singleton
    fun getRegistrationRepository(mainRepository: MainRepository): RegistrationRepository

    @Binds
    @Singleton
    fun getUserDetailRepository(mainRepository: MainRepository): UserDetailRepository

    @Binds
    @Singleton
    fun getMapRepository(mainRepository: MainRepository): MapRepository

    @Binds
    @Singleton
    fun getStorage(storage: SharedPreferenceStorage): Storage
}
