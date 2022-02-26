package com.kkk.moviereview.di

import com.kkk.moviereview.data.db.MyDatabase
import com.kkk.moviereview.network.ApiService
import com.kkk.moviereview.util.AppConstants
import com.kkk.mylibrary.network.createOkHttpClient
import com.kkk.mylibrary.network.createWebService
import org.koin.dsl.module

val remoteDatasourceModule = module  {
    single { createOkHttpClient() }
    single<ApiService> { createWebService(get(), AppConstants.baseUrl) }
}

val localDatasourceModule = module  {
    single { MyDatabase.getInstance(get()) }
}

val datasourceModule = listOf(remoteDatasourceModule, localDatasourceModule)