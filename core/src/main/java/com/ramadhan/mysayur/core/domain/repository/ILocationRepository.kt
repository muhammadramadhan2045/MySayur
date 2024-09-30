package com.ramadhan.mysayur.core.domain.repository

import com.ramadhan.mysayur.core.domain.model.LocationTracker

interface ILocationRepository {
    fun saveLocation(location: LocationTracker)
    fun deleteOldLocation()
    fun isDataExist(): Boolean
    fun getAllLocations(): List<LocationTracker>
}