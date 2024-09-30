package com.ramadhan.mysayur.core.domain.usecase

import com.ramadhan.mysayur.core.domain.model.LocationTracker
import com.ramadhan.mysayur.core.domain.repository.ILocationRepository

class LocationInteractor(
    private val locationRepository: ILocationRepository
): LocationUseCase {
    override fun saveLocation(location: LocationTracker) {
        locationRepository.saveLocation(location)
    }

    override fun deleteOldData() {
        locationRepository.deleteOldLocation()
    }

    override fun isDataExist(): Boolean {
        return locationRepository.isDataExist()
    }

    override fun getAllLocations(): List<LocationTracker> {
        return locationRepository.getAllLocations()
    }
}