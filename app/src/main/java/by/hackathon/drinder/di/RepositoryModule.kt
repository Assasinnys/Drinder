package by.hackathon.drinder.di

import by.hackathon.drinder.data.repository.*
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface RepositoryModule {

    @Binds
    @Singleton
    fun getLoginRepository(repository: Repository): LoginRepository

    @Binds
    @Singleton
    fun getRegistrationRepository(repository: Repository): RegistrationRepository

    @Binds
    @Singleton
    fun getUserDetailRepository(repository: Repository): UserDetailRepository

    @Binds
    @Singleton
    fun getMapRepository(repository: Repository): MapRepository
}