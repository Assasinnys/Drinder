package by.hackathon.drinder.di

import by.hackathon.drinder.data.repository.*
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {

    @Binds
    fun getLoginRepository(repository: Repository): LoginRepository

    @Binds
    fun getRegistrationRepository(repository: Repository): RegistrationRepository

    @Binds
    fun getUserDetailRepository(repository: Repository): UserDetailRepository

    @Binds
    fun getMapRepository(repository: Repository): MapRepository
}