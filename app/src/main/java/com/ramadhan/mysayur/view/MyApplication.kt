package com.ramadhan.mysayur.view

import android.app.Application
import com.ramadhan.mysayur.core.utils.scheduleCleanUp

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        scheduleCleanUp(this)
    }
}