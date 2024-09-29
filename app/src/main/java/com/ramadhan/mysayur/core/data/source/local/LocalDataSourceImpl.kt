package com.ramadhan.mysayur.core.data.source.local

import com.ramadhan.mysayur.core.data.source.LocalDataSource
import com.ramadhan.mysayur.core.data.source.local.db.LocationDatabaseHelper
import com.ramadhan.mysayur.core.domain.model.LocationTracker

class LocalDataSourceImpl(
    private val dbHelper: LocationDatabaseHelper
) : LocalDataSource {
    override fun saveLocation(location: LocationTracker) {
        dbHelper.saveLocation(location.latitude, location.longitude, location.timestamp)
    }

    override fun deleteOldData() {
        dbHelper.deleteOldData()
    }

    override fun getAllLocations(): List<LocationTracker> {
        return dbHelper.getAllLocations()
    }

    override fun isDataExist(): Boolean {
        return dbHelper.isDataExist()
    }
}