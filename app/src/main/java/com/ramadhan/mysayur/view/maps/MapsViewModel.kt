package com.ramadhan.mysayur.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ramadhan.mysayur.core.domain.model.LocationTracker
import com.ramadhan.mysayur.core.domain.usecase.LocationUseCase

class MapsViewModel(
    private val locationUseCase: LocationUseCase
) : ViewModel() {

    private val _locations: MutableLiveData<List<LocationTracker>> = MutableLiveData()
    val locations: LiveData<List<LocationTracker>> get() = _locations


    fun getAllLocations() {
        try {
            _locations.value = locationUseCase.getAllLocations()
        } catch (e: Exception) {
            Log.e("ListLocationViewModel", e.message.toString())
        }
    }

}