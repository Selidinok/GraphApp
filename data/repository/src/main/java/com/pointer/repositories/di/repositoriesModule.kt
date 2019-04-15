package com.pointer.repositories.di

import com.pointer.repositories.GraphRepository
import org.koin.dsl.module.module
import org.koin.experimental.builder.single

val repositoriesModule = module {
    single<GraphRepository>()
}