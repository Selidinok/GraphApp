package com.example.graphapp.di

import com.example.graphapp.navigation.ScreenRouterImpl
import com.example.graphapp.remote.remoteModule
import com.pointer.core.NetworkProvider
import com.pointer.core.base.ScreenRouter
import com.pointer.repositories.di.repositoriesModule
import com.example.form.di.formModule
import org.koin.dsl.module.module
import org.koin.experimental.builder.single
import ru.terrakok.cicerone.Cicerone

val appModule = module {
    val cicerone = Cicerone.create(ScreenRouterImpl())
    single { cicerone.router } bind ScreenRouter::class
    single { cicerone.navigatorHolder }
    single<NetworkProvider>()
}

val appComponent = listOf(
    appModule,
    remoteModule,
    repositoriesModule,
    formModule
)