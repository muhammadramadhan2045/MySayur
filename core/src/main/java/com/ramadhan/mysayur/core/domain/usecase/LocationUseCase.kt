package com.ramadhan.mysayur.core.domain.usecase

import com.ramadhan.mysayur.core.domain.model.LocationTracker

interface LocationUseCase {
    fun saveLocation(location: LocationTracker)
    fun deleteOldData()
    fun isDataExist(): Boolean
    fun getAllLocations(): List<LocationTracker>
}