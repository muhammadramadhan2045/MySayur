package com.ramadhan.mysayur.view.di

import com.ramadhan.mysayur.core.domain.usecase.LocationInteractor
import com.ramadhan.mysayur.core.domain.usecase.LocationUseCase
import com.ramadhan.mysayur.view.listlocation.ListLocationViewModel
import com.ramadhan.mysayur.view.maps.MapsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<LocationUseCase> {
        LocationInteractor(get())
    }
}


val viewModelModule = module {
    viewModel { ListLocationViewModel(get()) }
    viewModel { MapsViewModel(get()) }
}
