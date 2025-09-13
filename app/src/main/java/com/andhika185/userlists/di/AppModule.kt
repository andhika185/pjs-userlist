package com.andhika185.userlists.di

import com.andhika185.userlists.data.remote.ApiService
import com.andhika185.userlists.data.repository.UserRepositoryImpl
import com.andhika185.userlists.domain.repository.UserRepository
import com.andhika185.userlists.domain.usecase.GetUsersUseCase
import com.andhika185.userlists.presentation.UserListViewModel
import com.andhika185.userlists.domain.usecase.GetUserByIdUseCase // Import baru
import com.andhika185.userlists.presentation.detail.UserDetailViewModel // Import baru
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    single<UserRepository> {
        UserRepositoryImpl(get())
    }

    factory {
        GetUsersUseCase(get())
    }

    factory { GetUserByIdUseCase(get()) }

    viewModel {
        UserListViewModel(get())
    }

    viewModel { UserDetailViewModel(get(), get()) }
}