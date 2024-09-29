package com.ramadhan.mysayur

import android.app.Application
import com.ramadhan.mysayur.core.di.databaseModule
import com.ramadhan.mysayur.core.di.repositoryModule
import com.ramadhan.mysayur.core.utils.scheduleCleanUp
import com.ramadhan.mysayur.view.di.useCaseModule
import com.ramadhan.mysayur.view.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        scheduleCleanUp(this)

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(listOf(
                useCaseModule,
                viewModelModule,
                repositoryModule,
                databaseModule
            ))
        }
    }
}