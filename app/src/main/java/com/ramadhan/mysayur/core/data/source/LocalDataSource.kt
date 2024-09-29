package com.ramadhan.mysayur.core.data.source

import com.ramadhan.mysayur.core.domain.model.LocationTracker

interface LocalDataSource {
    fun saveLocation(location: LocationTracker)
    fun deleteOldData()
    fun getAllLocations(): List<LocationTracker>
    fun isDataExist(): Boolean
}