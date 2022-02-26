package com.kkk.moviereview.di

import com.kkk.moviereview.data.repositories.MainRepository
import com.kkk.moviereview.data.repositories.MainRepositoryImpl
import com.kkk.moviereview.viewmodel.MainViewModel
import com.kkk.mylibrary.network.rx.AndroidSchedulerProvider
import com.kkk.mylibrary.network.rx.SchedulerProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val movieModule = module {
    //provide data repository
    single<MainRepository> { MainRepositoryImpl(get(),get(),get(),get()) }

//    ViewModel for Home
    viewModel { MainViewModel(get(), get()) }

}

val rxModule = module {
    //provide schedule provider
    factory<SchedulerProvider> { AndroidSchedulerProvider() }
}

val appModule = listOf(rxModule, movieModule)+ datasourceModule