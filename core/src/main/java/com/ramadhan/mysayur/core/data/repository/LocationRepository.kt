package com.ramadhan.mysayur.core.data.repository

import com.ramadhan.mysayur.core.data.source.LocalDataSource
import com.ramadhan.mysayur.core.domain.model.LocationTracker
import com.ramadhan.mysayur.core.domain.repository.ILocationRepository

class LocationRepository(
    private val localDataSource: LocalDataSource
): ILocationRepository {
    override fun saveLocation(location: LocationTracker) {
        localDataSource.saveLocation(location)
    }

    override fun deleteOldLocation() {
        localDataSource.deleteOldData()
    }

    override fun isDataExist(): Boolean {
        return localDataSource.isDataExist()
    }

    override fun getAllLocations(): List<LocationTracker> {
        return localDataSource.getAllLocations()
    }

}