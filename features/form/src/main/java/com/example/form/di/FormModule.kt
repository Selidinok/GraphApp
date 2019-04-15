package com.example.form.di

import com.example.form.domian.GetPointsUseCase
import com.example.form.presentation.FormViewModel
import org.koin.androidx.viewmodel.experimental.builder.viewModel
import org.koin.dsl.module.module
import org.koin.experimental.builder.single

val formModule = module {
    viewModel<FormViewModel>()
    single<GetPointsUseCase>()
}