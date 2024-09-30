package com.ramadhan.mysayur.core.data.source.local.db

import android.provider.BaseColumns

object LocationContract {
    object LocationEntry : BaseColumns{
        const val TABLE_NAME = "location"
        const val COLUMN_LATITUDE = "latitude"
        const val COLUMN_LONGITUDE = "longitude"
        const val COLUMN_TIMESTAMP = "timestamp"
    }
}