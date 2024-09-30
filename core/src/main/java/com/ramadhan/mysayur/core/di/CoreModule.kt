package com.ramadhan.mysayur.core.di

import com.ramadhan.mysayur.core.data.repository.LocationRepository
import com.ramadhan.mysayur.core.data.source.LocalDataSource
import com.ramadhan.mysayur.core.data.source.local.LocalDataSourceImpl
import com.ramadhan.mysayur.core.data.source.local.db.LocationDatabaseHelper
import com.ramadhan.mysayur.core.domain.repository.ILocationRepository
import org.koin.dsl.module

val databaseModule = module {
    single {
        LocationDatabaseHelper(get())
    }
}


val repositoryModule = module {
    single<LocalDataSource> {
        LocalDataSourceImpl(get())
    }

    single<ILocationRepository> {
        LocationRepository(get())
    }
}